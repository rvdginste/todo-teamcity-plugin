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
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePatternMatcherFileVisitor extends SimpleFileVisitor<Path> {

    final private List<PathMatcher> includeMatchers = new ArrayList<>();
    final private List<PathMatcher> excludeMatchers = new ArrayList<>();
    final private List<Path> foundPaths = new ArrayList<>();
    final private InterruptionChecker interruptionChecker;

    public BasePatternMatcherFileVisitor(
            Path root,
            List<String> includes,
            List<String> excludes) {
        this(root, includes, excludes, null);
    }

    public BasePatternMatcherFileVisitor(
            Path root,
            List<String> includes,
            List<String> excludes,
            InterruptionChecker interruptionChecker) {

        // interruption checker
        this.interruptionChecker = interruptionChecker;

        // process includes
        for (String include : includes) {
            includeMatchers.add(getPathMatcher(root, include));
        }

        // process excludes
        for (String exclude : excludes) {
            excludeMatchers.add(getPathMatcher(root, exclude));
        }

    }

    protected abstract PathMatcher getPathMatcher(Path root, String pattern);

    private boolean isMatchFor(Path file, List<PathMatcher> matchers) {
        for (PathMatcher matcher : matchers) {
            if (matcher.matches(file)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIncluded(Path file) {
        return isMatchFor(file, includeMatchers);
    }

    private boolean isExcluded(Path file) {
        return isMatchFor(file, excludeMatchers);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (interruptionChecker != null && interruptionChecker.isInterrupted()) {
            return FileVisitResult.TERMINATE;
        }
        if (isIncluded(file) && !isExcluded(file)) {
            foundPaths.add(file);
        }
        return super.visitFile(file, attrs);
    }

    public List<Path> getFoundPaths() {
        return foundPaths;
    }

}
