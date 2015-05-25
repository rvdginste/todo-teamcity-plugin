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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TodoPatternScanner {

    final private Pattern minors;
    final private Pattern majors;
    final private Pattern criticals;
    final private UniversalDetector detector = new UniversalDetector(null);

    public TodoPatternScanner(
            List<String> minors,
            List<String> majors,
            List<String> criticals) {
        this.minors = buildPattern(minors);
        this.majors = buildPattern(majors);
        this.criticals = buildPattern(criticals);
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

    private TodoScanResult scan(Path file, Charset charset) throws IOException {

        long startTime = System.nanoTime();

        List<TodoLine> todos = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file, guessCharset(file, charset))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (criticals.matcher(line).matches()) {
                    todos.add(new TodoLine(lineNumber, TodoLevel.CRITICAL, line));
                } else if (majors.matcher(line).matches()) {
                    todos.add(new TodoLine(lineNumber, TodoLevel.MAJOR, line));
                } else if (minors.matcher(line).matches()) {
                    todos.add(new TodoLine(lineNumber, TodoLevel.MINOR, line));
                }
            }
        } catch (MalformedInputException e) {
            return scan(file, guessCharset(file, charset));
        }

        TodoLine[] todoLineArray = todos.toArray(new TodoLine[0]);
        long estimatedTime = (System.nanoTime() - startTime) / 1_000_000;

        return new TodoScanResult(file, charset, estimatedTime, todoLineArray);
    }

    public TodoScanResult scan(Path file) throws IOException {
        return scan(file, StandardCharsets.UTF_8);
    }
}
