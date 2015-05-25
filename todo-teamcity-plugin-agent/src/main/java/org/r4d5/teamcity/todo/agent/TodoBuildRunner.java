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

package org.r4d5.teamcity.todo.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoBuildRunner implements AgentBuildRunner {

    public TodoBuildRunner() {
        Loggers.AGENT.warn("TodoBuildRunner created.");
    }

    private List<String> getValuesFor(@NotNull Map<String, String> parameters, String parameter) {
        String value = parameters.get(parameter);
        if (value == null) {
            return new ArrayList<>();
        } else {
            return StringUtil.split(value, true, '\r', '\n');
        }
    }

    @NotNull
    public BuildProcess createBuildProcess(@NotNull final AgentRunningBuild build,
                                           @NotNull final BuildRunnerContext context) throws RunBuildException {

        Loggers.AGENT.warn("TodoBuildRunner: createBuildProcess.");

        final Map<String, String> runnerParameters = context.getRunnerParameters();

        final List<String> includes = getValuesFor(runnerParameters, TodoBuildRunnerConstants.PARAM_INCLUDE_REGEX);
        final List<String> excludes = getValuesFor(runnerParameters, TodoBuildRunnerConstants.PARAM_EXCLUDE_REGEX);

        final List<String> minors = getValuesFor(runnerParameters, TodoBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX);
        final List<String> majors = getValuesFor(runnerParameters, TodoBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX);
        final List<String> criticals = getValuesFor(runnerParameters, TodoBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX);

        final File root = build.getCheckoutDirectory();

        return new TodoBuildProcessAdapter(
                build.getBuildLogger(),
                root,
                includes,
                excludes,
                minors,
                majors,
                criticals);
    }

    @NotNull
    public AgentBuildRunnerInfo getRunnerInfo() {
        return new TodoBuildRunnerInfo();
    }
}
