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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TodoScanner {

    final private Path workingRoot;
    final private Path reportingRoot;
    final private List<String> includes;
    final private List<String> excludes;
    final private List<String> minors;
    final private List<String> majors;
    final private List<String> criticals;

    final private int contextBefore;
    final private int contextAfter;

    public TodoScanner(
            final Path workingRoot,
            final Path reportingRoot,
            final List<String> includes,
            final List<String> excludes,
            final List<String> minors,
            final List<String> majors,
            final List<String> criticals) throws IOException {
        this(workingRoot,
                reportingRoot,
                includes,
                excludes,
                minors,
                majors,
                criticals,
                2,
                5);
    }

    public TodoScanner(
            final Path workingRoot,
            final Path reportingRoot,
            final List<String> includes,
            final List<String> excludes,
            final List<String> minors,
            final List<String> majors,
            final List<String> criticals,
            final int contextBefore,
            final int contextAfter) throws IOException {
        this.workingRoot = workingRoot;
        this.reportingRoot = reportingRoot;
        this.includes = includes;
        this.excludes = excludes;
        this.minors = minors;
        this.majors = majors;
        this.criticals = criticals;
        this.contextBefore = contextBefore;
        this.contextAfter = contextAfter;
    }

    public void Run(InterruptionChecker interruptionChecker, StatusLogger statusLogger) throws IOException {

        // logging the settings
        statusLogger.info(String.format("Root path is [%1$s].", workingRoot.toString()));
        for (String include : includes) {
            statusLogger.info(String.format("Included is [%1$s].", include));
        }
        for (String exclude : excludes) {
            statusLogger.info(String.format("Excluded is [%1$s].", exclude));
        }

        // gather the files that need to be checked
        statusLogger.info("Start gathering the files to be checked.");
        GlobPatternMatcherFileVisitor visitor = new GlobPatternMatcherFileVisitor(workingRoot, includes, excludes, interruptionChecker);
        Files.walkFileTree(workingRoot, visitor);
        List<Path> foundPaths = visitor.getFoundRelativePaths();
        statusLogger.info(String.format("Gathered %1$d files.", foundPaths.size()));

        // execute the scanning
        interruptionChecker.isInterrupted();

        // logging the settings
        for (String minor : minors) {
            statusLogger.info(String.format("Minors include [%1$s].", minor));
        }
        for (String major : majors) {
            statusLogger.info(String.format("Majors include [%1$s].", major));
        }
        for (String critical : criticals) {
            statusLogger.info(String.format("Criticals include [%1$s].", critical));
        }

        // create todo scanner
        TodoPatternScanner scanner = new TodoPatternScanner(minors, majors, criticals, contextBefore, contextAfter);

        // scan for ToDo patterns
        ArrayList<TodoScanResult> scanResults = new ArrayList<>(foundPaths.size());
        for (Path path : foundPaths) {
            statusLogger.info(String.format("Scanning file [%1$s]", path.toString()));

            Path scanResultPath = reportingRoot.resolve(path);
            statusLogger.info(String.format("Scan result into [%1$s]", scanResultPath.toString()));

            final TodoScanResult result = scanner.scan(workingRoot, workingRoot.resolve(path));
            scanResults.add(result);
            interruptionChecker.isInterrupted();
        }

        // persist the results somewhere
        try {
            Path reportPath = Paths.get(reportingRoot.toString(), TodoBuildRunnerConstants.TODO_REPORTING_FILENAME);
            statusLogger.info(String.format("Storing TodoScanResults in [%1$s]", reportPath.toString()));
            FileOutputStream fileOut = new FileOutputStream(reportPath.toString());
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(scanResults);
            objectOut.close();
        } catch (IOException e) {
            // TODO
        }
    }
}
