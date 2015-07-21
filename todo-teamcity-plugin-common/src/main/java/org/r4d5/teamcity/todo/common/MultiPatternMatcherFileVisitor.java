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

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;

public class MultiPatternMatcherFileVisitor extends BasePatternMatcherFileVisitor {

    public MultiPatternMatcherFileVisitor(
            Path root,
            List<String> includes,
            List<String> excludes) {
        this(root, includes, excludes, null);
    }

    public MultiPatternMatcherFileVisitor(
            Path root,
            List<String> includes,
            List<String> excludes,
            InterruptionChecker interruptionChecker) {
        super(root, includes, excludes, interruptionChecker);
    }

    @Override
    protected PathMatcher getPathMatcher(String typeAndPattern) {

        int split = typeAndPattern.indexOf(':');
        String type = typeAndPattern.substring(0, split - 1);
        String pattern = typeAndPattern.substring(split + 1);

        return FileSystems
                .getDefault()
                .getPathMatcher(String.format("%1$s:%2$s", type, pattern));
    }
}
