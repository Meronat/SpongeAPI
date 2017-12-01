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
package org.spongepowered.api.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.entity.damage.DamageFunction;
import org.spongepowered.api.event.cause.entity.damage.DamageModifier;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class SpongeAbstractDamageEntityEventTest {

    private static final double ERROR = 0.03;

    @Test
    public void testParams() {
        Entity targetEntity = mockParam(Entity.class);
        int originalDamage = 5;

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(),"none"),
                Lists.newArrayList(), targetEntity, originalDamage);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalDamage, ERROR);

        assertEquals(event.getFinalDamage(), originalDamage, ERROR);
        assertEquals(event.getBaseDamage(), originalDamage, ERROR);
    }

    @Test
    public void testSetBaseDamage() {
        Entity targetEntity = mockParam(Entity.class);
        int originalDamage = 5;

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(),"none"),
                Lists.newArrayList(), targetEntity, originalDamage);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalDamage, ERROR);

        event.setBaseDamage(20);

        assertEquals(event.getBaseDamage(), 20, ERROR);
        assertEquals(event.getFinalDamage(), 20, ERROR);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalDamage, ERROR);
    }

    @Test
    public void testUseModifiers() {
        Entity targetEntity = mockParam(Entity.class);

        final int originalDamage = 1;
        final int originalFinalDamage = 18;

        final int firstModifierDamage = 2;
        final int secondModifierDamage = 15;

        DamageModifier firstModifier = mockParam(DamageModifier.class);
        DamageModifier secondModifier = mockParam(DamageModifier.class);

        List<DamageFunction>
                originalFunctions = Lists.newArrayList(DamageFunction.of(firstModifier, p -> p * 2), DamageFunction.of(secondModifier, p -> p * 5));

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(),"none"),
                originalFunctions, targetEntity, originalDamage);

        final List<DamageFunction> originalFunctions1 = event.getOriginalFunctions();
        assertIterableEquals(originalFunctions1, originalFunctions);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalFinalDamage, ERROR);

        Map<DamageModifier, Double> originalDamages = event.getOriginalDamages();

        assertEquals(originalDamages.size(), originalFunctions.size());

        assertEquals(originalDamages.get(firstModifier), firstModifierDamage, ERROR);
        assertEquals(originalDamages.get(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalModifierDamage(firstModifier), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalModifierDamage(secondModifier), secondModifierDamage, ERROR);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);
    }

    @Test
    public void testSetModifiers() {
        Entity targetEntity = mockParam(Entity.class);

        final int originalDamage = 1;
        final int originalFinalDamage = 18;

        final int firstModifierDamage = 2;
        final int secondModifierDamage = 15;

        final int firstChangedDamage = 1;
        final int secondChangedDamage = 10;

        final int modifiedFinalDamage = 12;

        DamageModifier firstModifer = mockParam(DamageModifier.class);
        DamageModifier secondModifier = mockParam(DamageModifier.class);

        List<DamageFunction>
                originalFunctions = Lists.newArrayList(DamageFunction.of(firstModifer, p -> p * 2), DamageFunction.of(secondModifier, p -> p * 5));

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(),"none"),
                originalFunctions, targetEntity, originalDamage);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        DoubleUnaryOperator newFunction = p -> p;

        event.setDamage(firstModifer, newFunction);

        assertEquals(event.getDamage(firstModifer), firstChangedDamage, ERROR);
        assertEquals(event.getDamage(secondModifier), secondChangedDamage, ERROR);

        assertEquals(event.getOriginalModifierDamage(firstModifer), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalModifierDamage(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalFinalDamage, ERROR);
        assertEquals(event.getFinalDamage(), modifiedFinalDamage, ERROR);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertIterableEquals(event.getModifiers(), Lists.newArrayList(DamageFunction.of(firstModifer, newFunction), originalFunctions.get(1)));
    }

    @Test
    public void testAddModifier() {
        Entity targetEntity = mockParam(Entity.class);

        final int originalDamage = 1;
        final int originalFinalDamage = 18;

        final int firstModifierDamage = 2;
        final int secondModifierDamage = 15;

        final int modifiedFinalDamage = 36;

        final int thirdDamage = 18;

        DamageModifier firstModifer = mockParam(DamageModifier.class);
        DamageModifier secondModifier = mockParam(DamageModifier.class);
        DamageModifier thirdModifier = mockParam(DamageModifier.class);

        DoubleUnaryOperator thirdFunction = p -> p;

        List<DamageFunction>
                originalFunctions = Lists.newArrayList(DamageFunction.of(firstModifer, p -> p * 2), DamageFunction.of(secondModifier, p -> p * 5));

        List<DamageFunction> newFunctions = Lists.newArrayList(originalFunctions);
        newFunctions.add(DamageFunction.of(thirdModifier, thirdFunction));

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(),"none"),
                originalFunctions, targetEntity, originalDamage);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertFalse(event.isModifierApplicable(thirdModifier));

        event.setDamage(thirdModifier, thirdFunction);

        assertEquals(event.getDamage(firstModifer), firstModifierDamage, ERROR);
        assertEquals(event.getDamage(secondModifier), secondModifierDamage, ERROR);
        assertEquals(event.getDamage(thirdModifier), thirdDamage, ERROR);

        assertEquals(event.getOriginalModifierDamage(firstModifer), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalModifierDamage(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalDamage(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalDamage(), originalFinalDamage, ERROR);
        assertEquals(event.getFinalDamage(), modifiedFinalDamage, ERROR);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertIterableEquals(event.getModifiers(), newFunctions);
    }

    @Test
    public void testModifiersApplicable() {
        Entity targetEntity = mockParam(Entity.class);

        DamageModifier firstModifer = mockParam(DamageModifier.class);
        DamageModifier secondModifier = mockParam(DamageModifier.class);

        List<DamageFunction>
                originalFunctions = Lists.newArrayList(DamageFunction.of(firstModifer, p -> p), DamageFunction.of(secondModifier, p -> p));

        DamageEntityEvent event = SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(), "none"), originalFunctions, targetEntity, 0);

        assertTrue(event.isModifierApplicable(firstModifer));
        assertTrue(event.isModifierApplicable(secondModifier));
        assertFalse(event.isModifierApplicable(mockParam(DamageModifier.class)));
    }

    @Test()
    public void testNotApplicableModifier() {
        assertThrows(IllegalArgumentException.class, () -> {
            DamageEntityEvent event =
                    SpongeEventFactory.createDamageEntityEvent(Cause.of(EventContext.empty(), "none"), Lists.newArrayList(), mockParam(Entity.class), 0);

            DamageModifier modifier = mockParam(DamageModifier.class);

            assertFalse(event.isModifierApplicable(modifier));

            event.getOriginalModifierDamage(modifier);
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T mockParam(Class<T> clazz) {
        return (T) SpongeEventFactoryTest.mockParam(clazz);
    }

}
