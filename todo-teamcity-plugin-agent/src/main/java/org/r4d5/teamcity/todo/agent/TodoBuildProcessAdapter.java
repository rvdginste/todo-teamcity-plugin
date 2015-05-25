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
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.messages.BuildMessage1;
import org.jetbrains.annotations.NotNull;
import org.r4d5.teamcity.todo.common.InterruptionChecker;
import org.r4d5.teamcity.todo.common.StatusLogger;
import org.r4d5.teamcity.todo.common.TodoScanner;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TodoBuildProcessAdapter extends AbstractBuildProcessAdapter {

    final File root;
    final List<String> includes;
    final List<String> excludes;
    final List<String> minors;
    final List<String> majors;
    final List<String> criticals;


    public TodoBuildProcessAdapter(
            @NotNull BuildProgressLogger logger,
            @NotNull File root,
            @NotNull List<String> includes,
            @NotNull List<String> excludes,
            @NotNull List<String> minors,
            @NotNull List<String> majors,
            @NotNull List<String> criticals) {
        super(logger);
        this.root = root;
        this.includes = includes;
        this.excludes = excludes;
        this.minors = minors;
        this.majors = majors;
        this.criticals = criticals;
    }

    @Override
    protected void runProcess() throws RunBuildException {
        try {
            final String rootPath = root.getCanonicalPath();
            progressLogger.warning(String.format("The Root path is [%1$s].", rootPath));
            final Path path = Paths.get(rootPath);
            final TodoScanner scanner = new TodoScanner(path, includes, excludes, minors, majors, criticals);
            final AtomicReference<Exception> scannerException = new AtomicReference<Exception>(null);
            final Runnable interruptibleScanner = new Runnable() {
                public void run() {
                    try {
                        scanner.Run(
                                new InterruptionChecker() {
                                    public boolean isInterrupted() {
                                        return TodoBuildProcessAdapter.this.isInterrupted();
                                    }
                                },
                                new StatusLogger() {
                                    @Override
                                    public void info(String message) {
                                        progressLogger.message(message);
                                    }
                                });
                    } catch (Exception e) {
                        progressLogger.error(e.getMessage());
                        scannerException.set(e);
                    }
                }
            };

            final Thread scannerThread = new Thread(interruptibleScanner);
            scannerThread.start();
            scannerThread.join();

            final Exception innerException = scannerException.get();
            if (innerException != null) {
                throw innerException;
            }

        } catch (RunBuildException e) {
            throw e;
        } catch (Exception e) {
            throw new RunBuildException(e);
        } finally {

        }

    }
}
