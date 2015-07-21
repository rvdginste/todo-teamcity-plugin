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

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class PathMatcherTests {

    @Test
    public void Test_GlobPatternMatcher_RelativePath_InRoot() {

        PathMatcher testMatcher = FileSystems
                .getDefault()
                .getPathMatcher("glob:**/*.sql");

        Path root = Paths.get("c:\\temp");
        Path examplePath = Paths.get("c:\\temp\\test\\blabla.sql");
        Path relativePath = root.relativize(examplePath);

        Assert.assertTrue(testMatcher.matches(relativePath));
    }

    @Test
    public void Test_GlobPatternMatcher_RelativePath_InFolderUnderRoot() {

        PathMatcher testMatcher = FileSystems
                .getDefault()
                .getPathMatcher("glob:**/*.sql");

        Path root = Paths.get("c:\\temp");
        Path examplePath = Paths.get("c:\\temp\\blabla.sql");
        Path relativePath = root.relativize(examplePath);

        Assert.assertFalse(testMatcher.matches(relativePath));
    }

    @Test
    public void Test_GlobPatternMatcher_AbsolutePath_InRoot() {

        Path root = Paths.get("c:\\temp");
        Path examplePath = Paths.get("c:\\temp\\blabla.sql");

        String pattern = String.format(
                "glob:%1$s%2$s%3$s",
                root.normalize().toString().replace('\\', '/'),
                "/",
                "**/*.sql");
        PathMatcher testMatcher = FileSystems
                .getDefault()
                .getPathMatcher(pattern);

        Assert.assertFalse(testMatcher.matches(examplePath));
    }

    @Test
    public void Test_GlobPatternMatcher_AbsolutePath_InFolderUnderRoot() {

        Path root = Paths.get("c:\\temp");
        Path examplePath = Paths.get("c:\\temp\\tra\\blabla.sql");

        String pattern = String.format(
                "glob:%1$s%2$s%3$s",
                root.normalize().toString().replace('\\', '/'),
                "/",
                "**/*.sql");
        PathMatcher testMatcher = FileSystems
                .getDefault()
                .getPathMatcher(pattern);

        Assert.assertTrue(testMatcher.matches(examplePath));
    }
}
