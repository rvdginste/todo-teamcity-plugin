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

public class TodoBuildRunnerConstants {
    public static final String TODO_RUN_TYPE = "todo-build-runner";

    public static final String PARAM_INCLUDE_REGEX = "org.r4d5.teamcity.todo.include";
    public static final String PARAM_EXCLUDE_REGEX = "org.r4d5.teamcity.todo.exclude";
    public static final String PARAM_PATTERN_MINOR_REGEX = "org.r4d5.teamcity.todo.minor";
    public static final String PARAM_PATTERN_MAJOR_REGEX = "org.r4d5.teamcity.todo.major";
    public static final String PARAM_PATTERN_CRITICAL_REGEX = "org.r4d5.teamcity.todo.critical";

    public static final String TODO_REPORTING_FOLDER = "org.r4d5.teamcity.todo.report";

    public static final String TODO_REPORTING_FILENAME = "TodoScannerResults.ser";

    public static final String TODO_REPORTING_IDENTIFICATION = "todoScanResultsReport";
}
