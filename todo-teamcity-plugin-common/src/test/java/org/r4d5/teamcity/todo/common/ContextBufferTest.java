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

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by ruben on 6/7/15.
 */
public class ContextBufferTest {

    @Test
    public void buffer_is_full_1() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        Assert.assertFalse(buffer.isFull());
    }

    @Test
    public void buffer_is_full_2() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        Assert.assertTrue(buffer.isFull());
    }

    @Test
    public void buffer_is_full_3() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        Assert.assertTrue(buffer.isFull());
    }

    @Test
    public void buffer_is_full_4() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.push("4");
        Assert.assertTrue(buffer.isFull());
    }

    @Test
    public void buffer_is_full_5() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.reset();
        Assert.assertFalse(buffer.isFull());
    }

    @Test
    public void buffer_getAt_1() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        Assert.assertEquals("1", buffer.getAt(-1));
    }

    @Test
    public void buffer_getAt_2() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        Assert.assertEquals("2", buffer.getAt(-1));
        Assert.assertEquals("1", buffer.getAt(-2));
    }

    @Test
    public void buffer_getAt_3() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        Assert.assertEquals("3", buffer.getAt(-1));
        Assert.assertEquals("2", buffer.getAt(-2));
    }

    @Test
    public void buffer_getAt_4() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.reset();
        Assert.assertNull(buffer.getAt(-1));
        Assert.assertNull(buffer.getAt(-2));
    }

    @Test
    public void buffer_getBufferSize_1() {
        ContextBuffer buffer = new ContextBuffer(2);
        Assert.assertEquals(2, buffer.getBufferSize());
    }

    @Test
    public void buffer_getBufferSize_2() {
        ContextBuffer buffer = new ContextBuffer(5);
        Assert.assertEquals(5, buffer.getBufferSize());
    }

    @Test
    public void buffer_getBufferReversed_1() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        String[] reverse = buffer.getBufferReversed();
        String[] expected = new String[]{ "2", "1" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBufferReversed_2() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        String[] reverse = buffer.getBufferReversed();
        String[] expected = new String[]{ "3", "2" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBufferReversed_3() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        String[] reverse = buffer.getBufferReversed();
        String[] expected = new String[]{ "1", null };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBufferReversed_4() {
        ContextBuffer buffer = new ContextBuffer(3);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.push("4");
        buffer.push("5");
        String[] reverse = buffer.getBufferReversed();
        String[] expected = new String[]{ "5", "4", "3" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBufferReversed_5() {
        ContextBuffer buffer = new ContextBuffer(3);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.push("4");
        buffer.reset();
        String[] reverse = buffer.getBufferReversed();
        String[] expected = new String[]{ null, null, null };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBuffer_1() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        String[] reverse = buffer.getBuffer();
        String[] expected = new String[]{ "1", "2" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBuffer_2() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        String[] reverse = buffer.getBuffer();
        String[] expected = new String[]{ "2", "3" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBuffer_3() {
        ContextBuffer buffer = new ContextBuffer(2);
        buffer.push("1");
        String[] reverse = buffer.getBuffer();
        String[] expected = new String[]{ null, "1" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBuffer_4() {
        ContextBuffer buffer = new ContextBuffer(3);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.push("4");
        buffer.push("5");
        String[] reverse = buffer.getBuffer();
        String[] expected = new String[]{ "3", "4", "5" };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }

    @Test
    public void buffer_getBuffer_5() {
        ContextBuffer buffer = new ContextBuffer(3);
        buffer.push("1");
        buffer.push("2");
        buffer.push("3");
        buffer.push("4");
        buffer.reset();
        String[] reverse = buffer.getBuffer();
        String[] expected = new String[]{ null, null, null };
        Assert.assertEquals(expected.length, reverse.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], reverse[i]);
        }
    }
}
