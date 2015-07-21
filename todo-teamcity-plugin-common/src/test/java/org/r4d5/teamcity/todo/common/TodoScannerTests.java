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

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Ignore
public class TodoScannerTests {

    @Test
    public void Test_1() throws IOException {

        DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("/tmp/bumpa"), "*.cs");

        for (Path p : paths) {
            System.out.println(p);
        }

    }

    @Test
    public void Test_2() throws IOException {

        Path root = Paths.get("c:\\temp");

        List<String> includes = new ArrayList<>();
        includes.add("**/*.sql");

        List<String> excludes = new ArrayList<>();
        excludes.add("*.java");

        GlobPatternMatcherFileVisitor visitor = new GlobPatternMatcherFileVisitor(root, includes, excludes);
        Files.walkFileTree(root, visitor);
        List<Path> foundPaths = visitor.getFoundPaths();
        Assert.assertNotNull(foundPaths);
    }

    @Test
    public void Test_Scanner() throws IOException {

        Path root = Paths.get("/tmp/affiliations");

        List<String> includes = new ArrayList<>();
        includes.add("*.{cs,ps1,txt,sql,nuspec,xml,xsl,xsd}");
        includes.add("**/*.{cs,ps1,txt,sql,nuspec,xml,xsl,xsd}");

        List<String> excludes = new ArrayList<>();

        GlobPatternMatcherFileVisitor visitor = new GlobPatternMatcherFileVisitor(root, includes, excludes);
        Files.walkFileTree(root, visitor);
        List<Path> foundPaths = visitor.getFoundPaths();

        List<String> minors = new ArrayList<>();
        minors.add(".*[Ii][Dd][Ee][Aa].*");
        List<String> majors = new ArrayList<>();
        majors.add(".*[Tt][Oo][Dd][Oo].*");
        List<String> criticals = new ArrayList<>();
        criticals.add(".*[Mm][Uu][Dd][Oo].*");
        TodoPatternScanner scanner = new TodoPatternScanner(minors, majors, criticals);

        long startTime = System.nanoTime();

        List<TodoScanResult> result = new ArrayList<>();
        for (Path file : foundPaths) {
            result.add(scanner.scan(Paths.get("/tmp/affiliations"), file));
        }

        long endTime = System.nanoTime();

        long spentTime = (endTime - startTime) / 1_000_000;

        Assert.assertFalse(result.isEmpty());

        for (TodoScanResult fileResult : result) {
            if (fileResult.getTodos().length > 0) {
                System.out.println(String.format("File: %s (%d ms)", fileResult.getFilePath(), fileResult.getRunTime()));
                for (TodoLine todo : fileResult.getTodos()) {
                    System.out.println(String.format("%5d : %10s %s", todo.getLineNumber(), todo.getLevel(), todo.getLine()));
                }
            }
        }

        Assert.assertFalse(result.isEmpty());
    }

}
