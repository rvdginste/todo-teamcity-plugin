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

package org.r4d5.teamcity.todo.server;

import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.util.StringUtil;
import org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class TodoPropertiesProcessor implements PropertiesProcessor {

    @Override
    public Collection<InvalidProperty> process(Map<String, String> properties) {

        Collection<InvalidProperty> result = new HashSet<InvalidProperty>();

        if (StringUtil.isEmptyOrSpaces(properties.get(TodoBuildRunnerConstants.PARAM_INCLUDE_REGEX))) {
            result.add(new InvalidProperty(TodoBuildRunnerConstants.PARAM_INCLUDE_REGEX, "The parameter 'include regex' must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(TodoBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX))) {
            result.add(new InvalidProperty(TodoBuildRunnerConstants.PARAM_PATTERN_MINOR_REGEX, "The parameter 'minor pattern' must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(TodoBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX))) {
            result.add(new InvalidProperty(TodoBuildRunnerConstants.PARAM_PATTERN_MAJOR_REGEX, "The parameter 'major pattern' must be specified."));
        }
        if (StringUtil.isEmptyOrSpaces(properties.get(TodoBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX))) {
            result.add(new InvalidProperty(TodoBuildRunnerConstants.PARAM_PATTERN_CRITICAL_REGEX, "The parameter 'critical pattern' must be specified."));
        }

        return result;
    }
}
