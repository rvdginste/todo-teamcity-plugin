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
import jetbrains.buildServer.agent.BuildFinishedStatus;
import jetbrains.buildServer.agent.BuildProcessAdapter;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.log.Loggers;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBuildProcessAdapter extends BuildProcessAdapter {

    protected final BuildProgressLogger progressLogger;

    private volatile boolean isFinished;
    private volatile boolean isFailed;
    private volatile boolean isInterrupted;

    protected AbstractBuildProcessAdapter(@NotNull final BuildProgressLogger logger) {
        progressLogger = logger;
        isFinished = false;
        isFailed = false;
        isInterrupted = false;
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
    }

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @NotNull
    @Override
    public BuildFinishedStatus waitFor() throws RunBuildException {
        while (!isInterrupted && !isFinished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RunBuildException(e);
            }
        }

        // MUDO BuildFinishedStatus.FINISHED_WITH_PROBLEMS ??

        return isFinished
                ? (isFailed ? BuildFinishedStatus.FINISHED_FAILED : BuildFinishedStatus.FINISHED_SUCCESS)
                : BuildFinishedStatus.INTERRUPTED;
    }

    @Override
    public void start() throws RunBuildException {
        try {
            runProcess();
        } catch (RunBuildException e) {
            progressLogger.buildFailureDescription(e.getMessage());
            Loggers.AGENT.error(e);
            isFailed = true;
        } finally {
            isFinished = true;
        }
    }

    protected abstract void runProcess() throws RunBuildException;
}
