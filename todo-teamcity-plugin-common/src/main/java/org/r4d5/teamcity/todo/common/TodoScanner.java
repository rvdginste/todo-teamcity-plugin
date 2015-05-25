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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TodoScanner {

    final private Path root;
    final private List<String> includes;
    final private List<String> excludes;
    final private List<String> minors;
    final private List<String> majors;
    final private List<String> criticals;


    public TodoScanner(
            final Path root,
            final List<String> includes,
            final List<String> excludes,
            final List<String> minors,
            final List<String> majors,
            final List<String> criticals) throws IOException {
        this.root = root;
        this.includes = includes;
        this.excludes = excludes;
        this.minors = minors;
        this.majors = majors;
        this.criticals = criticals;
    }

    public void Run(InterruptionChecker interruptionChecker, StatusLogger statusLogger) throws IOException {

        // logging the settings
        statusLogger.info(String.format("Root path is [%1$s].", root.toString()));
        for (String include : includes) {
            statusLogger.info(String.format("Included is [%1$s].", include));
        }
        for (String exclude : excludes) {
            statusLogger.info(String.format("Excluded is [%1$s].", exclude));
        }

        // gather the files that need to be checked
        statusLogger.info("Start gathering the files to be checked.");
        GlobPatternMatcherFileVisitor visitor = new GlobPatternMatcherFileVisitor(root, includes, excludes, interruptionChecker);
        Files.walkFileTree(root, visitor);
        List<Path> foundPaths = visitor.getFoundPaths();
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
        TodoPatternScanner scanner = new TodoPatternScanner(minors, majors, criticals);

        // scan for ToDo patterns
        for (Path path : foundPaths) {
            statusLogger.info(String.format("Scanning file [%1$s]", path.toString()));
            final TodoScanResult result = scanner.scan(path);
            int minor = 0, major = 0, critical = 0;
            for (TodoLine todo : result.getTodos()) {
                switch (todo.getLevel()) {
                    case MINOR:
                        minor++;
                        break;
                    case MAJOR:
                        major++;
                        break;
                    case CRITICAL:
                        critical++;
                        break;
                }
            }
            statusLogger.info(String.format("Found [%1$d minor, %2$d major, %3$d critical]", minor, major, critical));
            interruptionChecker.isInterrupted();
        }

        // persist the results somewhere
        interruptionChecker.isInterrupted();
    }
}
