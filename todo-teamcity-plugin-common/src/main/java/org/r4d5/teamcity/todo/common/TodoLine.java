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

import java.io.Serializable;

public class TodoLine implements Serializable {

    // stable serialization
    private static final long serialVersionUID = 4302086905378187385L;

    final int lineNumber;
    final TodoLevel level;
    final String line;

    public TodoLine(int lineNumber, TodoLevel level, String line) {
        this.lineNumber = lineNumber;
        this.level = level;
        this.line = line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public TodoLevel getLevel() {
        return level;
    }

    public String getLine() {
        return line;
    }
}
