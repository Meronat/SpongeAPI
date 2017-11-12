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
package org.spongepowered.api.data.type;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.util.annotation.CatalogedBy;

/**
 * Represents an equipment slot or hand slot on an armor stand that has
 * disabled removing, replacing, or placing.
 */
@CatalogedBy(DisabledSlotTypes.class)
public interface DisabledSlotType extends CatalogType {

    /**
     * Gets the {@link EquipmentType} of this disabled slot type.
     *
     * @return The equipment type of this disabled slot type
     */
    EquipmentType getEquipmentType();

    /**
     * Gets the {@link DisableType} of this disabled slot type.
     *
     * @return The disable type of this disabled slot type
     */
    DisableType getDisableType();

    /**
     * Represents whether a {@link DisabledSlotType} disables removing,
     * replacing, or placing items on an armor stand.
     */
    enum DisableType {

        /**
         * Disallows removing items on the armor stand.
         */
        REMOVE,

        /**
         * Disallows replacing items on the armor stand.
         */
        REPLACE,

        /**
         * Disallows placing items on the armor stand.
         */
        PLACE,

    }

}
