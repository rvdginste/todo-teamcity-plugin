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
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import org.jetbrains.annotations.NotNull;
import org.r4d5.teamcity.todo.common.InterruptionChecker;
import org.r4d5.teamcity.todo.common.StatusLogger;
import org.r4d5.teamcity.todo.common.TodoBuildRunnerConstants;
import org.r4d5.teamcity.todo.common.TodoScanner;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TodoBuildProcessAdapter extends AbstractBuildProcessAdapter {

    final ArtifactsWatcher artifactsWatcher;
    final File workingRoot;
    final File reportingRoot;
    final List<String> includes;
    final List<String> excludes;
    final List<String> minors;
    final List<String> majors;
    final List<String> criticals;


    public TodoBuildProcessAdapter(
            @NotNull ArtifactsWatcher artifactsWatcher,
            @NotNull BuildProgressLogger logger,
            @NotNull File workingRoot,
            @NotNull File reportingRoot,
            @NotNull List<String> includes,
            @NotNull List<String> excludes,
            @NotNull List<String> minors,
            @NotNull List<String> majors,
            @NotNull List<String> criticals) {
        super(logger);
        this.artifactsWatcher = artifactsWatcher;
        this.workingRoot = workingRoot;
        this.reportingRoot = reportingRoot;
        this.includes = includes;
        this.excludes = excludes;
        this.minors = minors;
        this.majors = majors;
        this.criticals = criticals;
    }

    @Override
    protected void runProcess() throws RunBuildException {
        try {
            // initialize working root path
            final String workingRootCanonicalPath = workingRoot.getCanonicalPath();
            progressLogger.message(String.format("The working root path is [%1$s].", workingRootCanonicalPath));
            final Path workingRootPath = Paths.get(workingRootCanonicalPath);

            // initialize reporting root path
            final String reportingRootCanonicalPath = reportingRoot.getCanonicalPath();
            progressLogger.message(String.format("The reporting root path is [%1$s].", reportingRootCanonicalPath));
            final Path reportingRootPath = Paths.get(reportingRootCanonicalPath, TodoBuildRunnerConstants.TODO_REPORTING_FOLDER);
            Files.createDirectory(reportingRootPath);
            progressLogger.message(String.format("Create directory for reporting [%1$s].", reportingRootPath));

            // initialize the Todo Scanner
            final TodoScanner scanner = new TodoScanner(
                    workingRootPath,
                    reportingRootPath,
                    includes,
                    excludes,
                    minors,
                    majors,
                    criticals);
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

            // run the Todo Scanner
            final Thread scannerThread = new Thread(interruptibleScanner);
            scannerThread.start();
            scannerThread.join();

            // register artifacts
            artifactsWatcher.addNewArtifactsPath(reportingRootPath.toString());

            // handle exceptions
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
