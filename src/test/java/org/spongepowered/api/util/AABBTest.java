/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AABBTest {

    private static final Random RANDOM = new Random();

    @Test
    public void testConstructor() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertEquals(new Vector3d(1, 2, 3), aabb1.getMin());
        assertEquals(new Vector3d(7, 10, 13), aabb1.getMax());

        final AABB aabb2 = new AABB(new Vector3d(11, 2, 3), new Vector3d(7, -10, 13));
        assertEquals(new Vector3d(7, -10, 3), aabb2.getMin());
        assertEquals(new Vector3d(11, 2, 13), aabb2.getMax());

        try {
            new AABB(new Vector3d(1, 2, 3), new Vector3d(1, 10, 13));
            Assertions.fail("Creation of AABB failed.");
        } catch (IllegalArgumentException ignored) {
            // pass
        }

        try {
            new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 2, 13));
            Assertions.fail("Creation of AABB failed.");
        } catch (IllegalArgumentException ignored) {
            // pass
        }

        try {
            new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 3));
            Assertions.fail("Creation of AABB failed.");
        } catch (IllegalArgumentException ignored) {
            // pass
        }

        final AABB aabb3 = new AABB(1, 2, 3, 7, 10, 13);
        assertEquals(new Vector3d(1, 2, 3), aabb3.getMin());
        assertEquals(new Vector3d(7, 10, 13), aabb3.getMax());
    }

    @Test
    public void testSize() {
        final AABB aabb = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertEquals(new Vector3d(6, 8, 10), aabb.getSize());
    }

    @Test
    public void testCenter() {
        final AABB aabb = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertEquals(new Vector3d(4, 6, 8), aabb.getCenter());
    }

    @Test
    public void testContainsCoordinates() {
        final AABB aabb = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertTrue(aabb.contains(5, 3, 11));
        assertTrue(aabb.contains(7, 3, 11));
        assertTrue(aabb.contains(5, 4, 11));
        assertTrue(aabb.contains(5, 3, 13));
        assertFalse(aabb.contains(-1, 3, 11));
        assertFalse(aabb.contains(5, 11, 11));
        assertFalse(aabb.contains(5, 3, 14));
    }

    @Test
    public void testContainsVector3d() {
        final AABB aabb = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertTrue(aabb.contains(new Vector3d(5, 3, 11)));
        assertFalse(aabb.contains(new Vector3d(-1, 3, 11)));
        assertFalse(aabb.contains(new Vector3d(5, 11, 11)));
        assertFalse(aabb.contains(new Vector3d(5, 3, 14)));
    }

    @Test
    public void testContainsVector3i() {
        final AABB aabb = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        assertTrue(aabb.contains(new Vector3i(5, 3, 11)));
        assertFalse(aabb.contains(new Vector3i(-1, 3, 11)));
        assertFalse(aabb.contains(new Vector3i(5, 11, 11)));
        assertFalse(aabb.contains(new Vector3i(5, 3, 14)));
    }

    @Test
    public void testIntersectsAABB() {
        for (int i = 0; i < 1000; i++) {
            final AABB aabb1 = newAABB();
            final AABB aabb2 = newIntersectingAABB(aabb1);
            final AABB aabb3 = aabb2.offset(aabb1.getSize().add(aabb2.getSize()));
            assertTrue(aabb1.intersects(aabb2));
            assertTrue(aabb2.intersects(aabb1));
            assertFalse(aabb1.intersects(aabb3));
            assertFalse(aabb3.intersects(aabb1));
        }
    }

    @Test
    public void testIntersectsRay() {
        final AABB aabb = new AABB(new Vector3d(0, 0, 0), new Vector3d(2, 2, 2));
        assertEquals(new Tuple<>(new Vector3d(2, 1, 1), new Vector3d(1, 0, 0)),
            aabb.intersects(new Vector3d(1, 1, 1), new Vector3d(1, 0, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 2, 1), new Vector3d(0, 1, 0)),
            aabb.intersects(new Vector3d(1, 1, 1), new Vector3d(0, 1, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 1, 2), new Vector3d(0, 0, 1)),
            aabb.intersects(new Vector3d(1, 1, 1), new Vector3d(0, 0, 1)).get());
        assertEquals(new Tuple<>(new Vector3d(0, 0, 0), new Vector3d(-1, -1, -1).normalize()),
            aabb.intersects(new Vector3d(-1, -1, -1), new Vector3d(1, 1, 1)).get());
        assertEquals(new Tuple<>(new Vector3d(0, 0, 1), new Vector3d(-1, -1, -0.0).normalize()),
            aabb.intersects(new Vector3d(-1, -1, 1), new Vector3d(1, 1, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(0, 1, 1), new Vector3d(-1, -0.0, -0.0)),
            aabb.intersects(new Vector3d(-1, 1, 1), new Vector3d(1, 0, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(2, 1, 1), new Vector3d(1, 0, 0)),
            aabb.intersects(new Vector3d(3, 1, 1), new Vector3d(-1, 0, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 0, 1), new Vector3d(-0.0, -1, -0.0)),
            aabb.intersects(new Vector3d(1, -1, 1), new Vector3d(0, 1, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 2, 1), new Vector3d(0, 1, 0)),
            aabb.intersects(new Vector3d(1, 3, 1), new Vector3d(0, -1, 0)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 1, 0), new Vector3d(-0.0, -0.0, -1)),
            aabb.intersects(new Vector3d(1, 1, -1), new Vector3d(0, 0, 1)).get());
        assertEquals(new Tuple<>(new Vector3d(1, 1, 2), new Vector3d(0, 0, 1)),
            aabb.intersects(new Vector3d(1, 1, 3), new Vector3d(0, 0, -1)).get());
        assertFalse(aabb.intersects(new Vector3d(-1, -1, -1), new Vector3d(0, 1, 0)).isPresent());
    }

    @Test
    public void testOffsetCoordinates() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(11, 0, 4), new Vector3d(17, 8, 14));
        assertEquals(aabb2, aabb1.offset(10, -2, 1));
    }

    @Test
    public void testOffsetVector3d() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(11, 0, 4), new Vector3d(17, 8, 14));
        assertEquals(aabb2, aabb1.offset(new Vector3d(10, -2, 1)));
    }

    @Test
    public void testOffsetVector3i() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(11, 0, 4), new Vector3d(17, 8, 14));
        assertEquals(aabb2, aabb1.offset(new Vector3i(10, -2, 1)));
    }

    @Test
    public void testExpandCoordinates() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(-4, 3, 2.5), new Vector3d(12, 9, 13.5));
        assertEquals(aabb2, aabb1.expand(10, -2, 1));
    }

    @Test
    public void testExpandVector3d() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(-4, 3, 2.5), new Vector3d(12, 9, 13.5));
        assertEquals(aabb2, aabb1.expand(new Vector3d(10, -2, 1)));
    }

    @Test
    public void testExpandVector3i() {
        final AABB aabb1 = new AABB(new Vector3d(1, 2, 3), new Vector3d(7, 10, 13));
        final AABB aabb2 = new AABB(new Vector3d(-4, 3, 2.5), new Vector3d(12, 9, 13.5));
        assertEquals(aabb2, aabb1.expand(new Vector3i(10, -2, 1)));
    }

    private static AABB newAABB() {
        final Vector3d min = new Vector3d(RANDOM.nextDouble() * 20 - 10, RANDOM.nextDouble() * 20 - 10, RANDOM.nextDouble() * 20 - 10);
        return new AABB(min, min.add(RANDOM.nextDouble() * 4 + 4, RANDOM.nextDouble() * 4 + 4, RANDOM.nextDouble() * 4 + 4));
    }

    private static AABB newIntersectingAABB(AABB with) {
        final Vector3d wMin = with.getMin();
        final Vector3d wSize = with.getSize();
        final double iSizeX = RANDOM.nextDouble() * wSize.getX();
        final double iSizeY = RANDOM.nextDouble() * wSize.getY();
        final double iSizeZ = RANDOM.nextDouble() * wSize.getZ();
        final double eSizeX = RANDOM.nextDouble() * 4 + 4;
        final double eSizeY = RANDOM.nextDouble() * 4 + 4;
        final double eSizeZ = RANDOM.nextDouble() * 4 + 4;
        final Vector3d min = wMin.sub(eSizeX, eSizeY, eSizeZ);
        final Vector3d max = wMin.add(iSizeX, iSizeY, iSizeZ);
        return new AABB(min, max);
    }

}
