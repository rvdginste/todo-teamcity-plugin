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

import java.nio.charset.Charset;
import java.nio.file.Path;

public class TodoScanResult {

    final Path file;
    final Charset charset;
    final long runTime;
    final TodoLine[] todos;

    public TodoScanResult(Path file, Charset charset, long runTime, TodoLine[] todos) {
        this.file = file;
        this.charset = charset;
        this.runTime = runTime;
        this.todos = todos;
    }

    public Path getFile() {
        return file;
    }

    public Charset getCharset() {
        return charset;
    }

    public TodoLine[] getTodos() {
        return todos;
    }

    public long getRunTime() {
        return runTime;
    }
}
