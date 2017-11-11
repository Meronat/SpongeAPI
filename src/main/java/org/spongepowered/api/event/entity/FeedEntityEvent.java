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
package org.spongepowered.api.event.entity;

import org.spongepowered.api.data.manipulator.mutable.entity.AgeableData;
import org.spongepowered.api.entity.living.animal.Animal;

/**
 * An event which represents when specific {@link Animal}s are fed.
 */
public interface FeedEntityEvent extends InteractEntityEvent {

    /**
     * Fired when an adult {@link Animal} is fed with a breeding item causing
     * it to be set in love.
     *
     * <p>This can be caused by trying to breed animals. Generally this is
     * just with their respectable breeding foods, with horses, wolves,
     * ocelots (cats), and llamas required to be tamed beforehand, and a wolf
     * must be at full health as well for it to be set in love mode.</p>
     */
    interface Love extends FeedEntityEvent {}

    /**
     * Fired when a passive, tameable {@link Animal} is fed, causing there to
     * be a chance of the animal being tamed. If the animal ends up successfully
     * tamed, a {@link TameEntityEvent} will be thrown.
     */
    interface Tame extends FeedEntityEvent {}

    /**
     * Fired when an {@link Animal} is fed by food which heals it.
     */
    interface Heal extends FeedEntityEvent {}

    /**
     * Fired when a child {@link Animal} is fed by one of its breeding items,
     * leading to aging.
     */
    interface Age extends FeedEntityEvent {

        AgeableData currentAge();

        AgeableData finalAge();

        void setFinalAge(AgeableData age);

    }

}
