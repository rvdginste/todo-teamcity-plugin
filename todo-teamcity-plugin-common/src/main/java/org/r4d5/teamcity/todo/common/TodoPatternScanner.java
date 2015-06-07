/*
 * Copyright (c) 2015  Ruben Vandeginste <ruben.vandeginste@gmail.com>
 *
 * This is part of the Todo TeamCity plugin.
 *
 * The Todo TeamCity plugin is free software: you can redistribute it
 * and/or modify it under the terms of the MIT license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * MIT license for more details.
 *
 * You should have received a copy of the MIT license along with
 * this program.  If not, see <http://opensource.org/licenses/MIT>.
 */

package org.r4d5.teamcity.todo.common;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

public class TodoPatternScanner {

    final private static Comparator<TodoLine> todoLineComparater = new Comparator<TodoLine>() {
        @Override
        public int compare(TodoLine todoLine1, TodoLine todoLine2) {
            return todoLine1.getLineNumber() - todoLine2.getLineNumber();
        }
    };

    final private Pattern minors;
    final private Pattern majors;
    final private Pattern criticals;

    final private int contextBefore;
    final private int contextAfter;

    final private UniversalDetector detector = new UniversalDetector(null);

    public TodoPatternScanner(
            List<String> minors,
            List<String> majors,
            List<String> criticals) {
        this(minors, majors, criticals, 0, 0);
    }


    public TodoPatternScanner(
            List<String> minors,
            List<String> majors,
            List<String> criticals,
            int contextBefore,
            int contextAfter) {
        this.minors = buildPattern(minors);
        this.majors = buildPattern(majors);
        this.criticals = buildPattern(criticals);
        this.contextBefore = contextBefore;
        this.contextAfter = contextAfter;
    }

    private Pattern buildPattern(List<String> regexes) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String regex : regexes) {
            if (first) {
                first = false;
            } else {
                sb.append('|');
            }
            sb.append(regex);
        }
        return Pattern.compile(sb.toString());
    }

    private Charset guessCharsetChardet(Path file, Charset originalAttempt) throws IOException {
        String detectedCharset = null;
        try (SeekableByteChannel byteChannel = Files.newByteChannel(file, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(16384);
            int count;
            while ((count = byteChannel.read(buffer)) > 0 && !detector.isDone()) {
                detector.handleData(buffer.array(), 0, count);
            }
        } finally {
            detector.dataEnd();
            detectedCharset = detector.getDetectedCharset();
        }
        return detectedCharset != null
                ? Charset.forName(detector.getDetectedCharset())
                : (originalAttempt == StandardCharsets.UTF_8
                ? StandardCharsets.US_ASCII
                : (originalAttempt == StandardCharsets.US_ASCII
                ? StandardCharsets.ISO_8859_1
                : null));
    }

    private Charset guessCharset(Path file, Charset charset) throws IOException {

        CharsetDetector detector = new CharsetDetector();
        byte[] data;

        try (SeekableByteChannel byteChannel = Files.newByteChannel(file, StandardOpenOption.READ)) {
            long size = byteChannel.size();

            if (size >= Integer.MAX_VALUE) {
                return guessCharsetChardet(file, charset);
            }

            int smallsize = (int) size;
            ByteBuffer buffer = ByteBuffer.allocate(smallsize);
            byteChannel.read(buffer);
            data = buffer.array();
        }

        detector.setText(data);
        CharsetMatch match = detector.detect();

        return Charset.forName(match.getName());
    }

    private TodoScanResult scan(Path workRoot, Path file, Charset charset) throws IOException {

        long startTime = System.nanoTime();

        HashMap<Integer, TodoLine> todoMap = new HashMap<>(10);
        ContextBuffer buffer = new ContextBuffer(contextBefore);

        try (BufferedReader reader = Files.newBufferedReader(file, guessCharset(file, charset))) {
            String line;
            int lineNumber = 0;
            int match = 0;

            while ((line = reader.readLine()) != null) {
                // context
                lineNumber++;
                TodoLine todoLine = null;

                // matching
                if (criticals.matcher(line).matches()) {
                    todoLine = new TodoLine(lineNumber, TodoLevel.CRITICAL, line);
                } else if (majors.matcher(line).matches()) {
                    todoLine = new TodoLine(lineNumber, TodoLevel.MAJOR, line);
                } else if (minors.matcher(line).matches()) {
                    todoLine = new TodoLine(lineNumber, TodoLevel.MINOR, line);
                }

                // found match ?
                if (todoLine != null) {
                    // remember the todoLine
                    todoMap.put(lineNumber, todoLine);

                    // fix context before
                    String[] reverse = buffer.getBufferReversed();
                    for (int i = 0; i < contextBefore; i++) {
                        int currentLineNumber = lineNumber - i - 1;
                        if (!todoMap.containsKey(currentLineNumber) && reverse[i] != null) {
                            todoMap.put(currentLineNumber, new TodoLine(currentLineNumber, TodoLevel.CONTEXT, reverse[i]));
                        }
                    }

                    // prepare context after
                    match = contextAfter;
                } else {
                    // context after match ?
                    if (match > 0) {
                        todoMap.put(lineNumber, new TodoLine(lineNumber, TodoLevel.CONTEXT, line));
                        match--;
                    }
                }

                // remember current line in the buffer
                buffer.push(line);
            }
        } catch (MalformedInputException e) {
            return scan(workRoot, file, guessCharset(file, charset));
        }

        ArrayList<TodoLine> todoLines = new ArrayList<>(todoMap.values());
        Collections.sort(todoLines, todoLineComparater);
        TodoLine[] todoLineArray = todoLines.toArray(new TodoLine[0]);
        long estimatedTime = (System.nanoTime() - startTime) / 1_000_000;

        return new TodoScanResult(workRoot.relativize(file), charset, estimatedTime, todoLineArray);
    }

    public TodoScanResult scan(Path workRoot, Path file) throws IOException {
        return scan(workRoot, file, StandardCharsets.UTF_8);
    }
}
