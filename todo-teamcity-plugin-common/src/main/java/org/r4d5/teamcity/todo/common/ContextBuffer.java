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

class ContextBuffer {

    final private int bufferSize;
    final private String[] buffer;

    private int index = 0;
    private boolean full = false;

    ContextBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new String[bufferSize];
    }

    void push(String line) {
        // adding value
        buffer[index] = line;

        // fixing index and full
        index++;
        if (index == bufferSize) {
            full = true;
            index = 0;
        }
    }

    String getAt(int pos) {
        assert pos < 0;

        int bufferIndex = (index + pos + bufferSize) % bufferSize;
        return buffer[bufferIndex];
    }

    String[] getBufferReversed() {
        String[] result = new String[bufferSize];

        for (int i = 0; i < bufferSize; i++) {
            result[i] = getAt(-i - 1);
        }

        return result;
    }

    String[] getBuffer() {
        String[] result = new String[bufferSize];

        for (int i = 0; i < bufferSize; i++) {
            result[i] = getAt(-bufferSize + i);
        }

        return result;
    }

    int getBufferSize() {
        return bufferSize;
    }

    void reset() {
        index = 0;
        full = false;
        for (int i = 0; i < bufferSize; i++) {
            buffer[i] = null;
        }
    }

    boolean isFull() {
        return full;
    }
}
