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
import org.spongepowered.api.event.cause.entity.health.HealthFunction;
import org.spongepowered.api.event.cause.entity.health.HealthModifier;
import org.spongepowered.api.event.entity.HealEntityEvent;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;

public class SpongeAbstractHealEntityEventTest {

    private static final double ERROR = 0.03;

    @Test
    public void testParams() {
        Entity targetEntity = mockParam(Entity.class);
        int originalDamage = 5;

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(),"none"),
                Lists.newArrayList(), targetEntity, originalDamage);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalDamage, ERROR);

        assertEquals(event.getFinalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getBaseHealAmount(), originalDamage, ERROR);
    }

    @Test
    public void testSetBaseHealAmount() {
        Entity targetEntity = mockParam(Entity.class);
        int originalDamage = 5;

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(),"none"),
                Lists.newArrayList(), targetEntity, originalDamage);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalDamage, ERROR);

        event.setBaseHealAmount(20);

        assertEquals(event.getBaseHealAmount(), 20, ERROR);
        assertEquals(event.getFinalHealAmount(), 20, ERROR);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalDamage, ERROR);
    }

    @Test
    public void testUseModifiers() {
        Entity targetEntity = mockParam(Entity.class);

        final int originalDamage = 1;
        final int originalFinalDamage = 18;

        final int firstModifierDamage = 2;
        final int secondModifierDamage = 15;

        HealthModifier firstModifier = mockParam(HealthModifier.class);
        HealthModifier secondModifier = mockParam(HealthModifier.class);

        List<HealthFunction>
                originalFunctions = Lists.newArrayList(HealthFunction.of(firstModifier, p -> p * 2), HealthFunction.of(secondModifier, p -> p * 5));

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(), "none"), originalFunctions, targetEntity,
                originalDamage);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalFinalDamage, ERROR);

        Map<HealthModifier, Double> originalDamages = event.getOriginalHealingAmounts();

        assertEquals(originalDamages.size(), originalFunctions.size());

        assertEquals(originalDamages.get(firstModifier), firstModifierDamage, ERROR);
        assertEquals(originalDamages.get(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalHealingModifierAmount(firstModifier), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalHealingModifierAmount(secondModifier), secondModifierDamage, ERROR);

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

        HealthModifier firstModifier = mockParam(HealthModifier.class);
        HealthModifier secondModifier = mockParam(HealthModifier.class);

        List<HealthFunction>
                originalFunctions = Lists.newArrayList(HealthFunction.of(firstModifier, p -> p * 2), HealthFunction.of(secondModifier, p -> p * 5));

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(), "none"), originalFunctions, targetEntity,
                originalDamage);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        DoubleUnaryOperator newFunction = p -> p;

        event.setHealAmount(firstModifier, newFunction);

        assertEquals(event.getHealAmount(firstModifier), firstChangedDamage, ERROR);
        assertEquals(event.getHealAmount(secondModifier), secondChangedDamage, ERROR);

        assertEquals(event.getOriginalHealingModifierAmount(firstModifier), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalHealingModifierAmount(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalFinalDamage, ERROR);
        assertEquals(event.getFinalHealAmount(), modifiedFinalDamage, ERROR);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertIterableEquals(event.getModifiers(), Lists.newArrayList(HealthFunction.of(firstModifier, newFunction), originalFunctions.get(1)));
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

        HealthModifier firstModifer = mockParam(HealthModifier.class);
        HealthModifier secondModifier = mockParam(HealthModifier.class);
        HealthModifier thirdModifier = mockParam(HealthModifier.class);

        DoubleUnaryOperator thirdFunction = p -> p;

        List<HealthFunction>
                originalFunctions = Lists.newArrayList(HealthFunction.of(firstModifer, p -> p * 2), HealthFunction.of(secondModifier, p -> p * 5));

        List<HealthFunction> newFunctions = Lists.newArrayList(originalFunctions);
        newFunctions.add(HealthFunction.of(thirdModifier, thirdFunction));

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(), "none"), originalFunctions, targetEntity,
                originalDamage);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertFalse(event.isModifierApplicable(thirdModifier));

        event.setHealAmount(thirdModifier, thirdFunction);

        assertEquals(event.getHealAmount(firstModifer), firstModifierDamage, ERROR);
        assertEquals(event.getHealAmount(secondModifier), secondModifierDamage, ERROR);
        assertEquals(event.getHealAmount(thirdModifier), thirdDamage, ERROR);

        assertEquals(event.getOriginalHealingModifierAmount(firstModifer), firstModifierDamage, ERROR);
        assertEquals(event.getOriginalHealingModifierAmount(secondModifier), secondModifierDamage, ERROR);

        assertEquals(event.getOriginalHealAmount(), originalDamage, ERROR);
        assertEquals(event.getOriginalFinalHealAmount(), originalFinalDamage, ERROR);
        assertEquals(event.getFinalHealAmount(), modifiedFinalDamage, ERROR);

        assertIterableEquals(event.getOriginalFunctions(), originalFunctions);

        assertIterableEquals(event.getOriginalFunctions(), newFunctions);
    }

    @Test
    public void testModifiersApplicable() {
        Entity targetEntity = mockParam(Entity.class);

        HealthModifier firstModifier = mockParam(HealthModifier.class);
        HealthModifier secondModifier = mockParam(HealthModifier.class);

        List<HealthFunction>
                originalFunctions = Lists.newArrayList(HealthFunction.of(firstModifier, p -> p), HealthFunction.of(secondModifier, p -> p));

        HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(), "none"), originalFunctions, targetEntity, 0);

        assertTrue(event.isModifierApplicable(firstModifier));
        assertTrue(event.isModifierApplicable(secondModifier));
        assertFalse(event.isModifierApplicable(mockParam(HealthModifier.class)));
    }

    @Test
    public void testNotApplicableModifer() {
        assertThrows(IllegalArgumentException.class, () -> {
            HealEntityEvent event = SpongeEventFactory.createHealEntityEvent(Cause.of(EventContext.empty(), "none"), Lists.newArrayList(),
                    mockParam(Entity.class), 0);

            HealthModifier modifier = mockParam(HealthModifier.class);

            assertFalse(event.isModifierApplicable(modifier));

            event.getOriginalHealingModifierAmount(modifier);
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T mockParam(Class<T> clazz) {
        return (T) SpongeEventFactoryTest.mockParam(clazz);
    }

}