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

import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TodoLogTab extends ViewLogTab {

    public TodoLogTab(
            @NotNull final PagePlaces pagePlaces,
            @NotNull final SBuildServer server,
            @NotNull final PluginDescriptor descriptor) {
        super("Todo Build Runner", "org.r4d5.teamcity.todo.log", pagePlaces, server);
        setIncludeUrl(descriptor.getPluginResourcesPath("todoLogTab.jsp"));
    }

    @Override
    protected void fillModel(Map<String, Object> model, HttpServletRequest request, SBuild build) {
        // add data here
    }
}
