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
package org.spongepowered.api.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.spongepowered.api.text.action.TextActions.insertText;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.test.TestHooks;

public class TextTest {

    @BeforeEach
    public void initialize() throws Exception {
        TestPlainTextSerializer.inject();
        TestHooks.mockFields(TextColors.class, TextColor.class);
        TestHooks.mockFields(TextStyles.class, TextStyle.class);
    }

    @Test
    public void testTextOf() {
        Text text = Text.of(TextColors.RED, "Red");
        assertEquals(text.toPlain(), "Red");

        text = findText(text, "Red");
        assertEquals(text.getColor(), TextColors.RED);
        assertTrue(text.getStyle().isEmpty());
        assertTrue(text.getChildren().isEmpty());
    }

    @Test
    public void testNestedTextOf() {
        Text text = Text.of(TextColors.RED, "Red", TextColors.YELLOW, "Yellow");
        assertEquals(text.toPlain(), "RedYellow");

        Text red = findText(text, "Red");
        assertEquals(red.toPlain(), "Red");
        assertEquals(red.getColor(), TextColors.RED);

        Text yellow = findText(text, "Yellow");
        assertEquals(yellow.toPlain(), "Yellow");
        assertEquals(yellow.getColor(), TextColors.YELLOW);
    }

    @Test
    public void testSimpleCompositeText() {
        Text text = Text.of(TextColors.YELLOW, Text.of("White"));
        assertEquals(text.toPlain(), "White");

        text = findText(text, "White");
        assertEquals(text.getColor(), TextColors.WHITE);
    }

    @Test
    public void testCompositeText() {
        Text text = Text.of(TextColors.GREEN, insertText("Welcome Spongie!"), "Welcome ", Text.of(TextColors.YELLOW, "Spongie"), " to the server!");
        assertEquals(text.toPlain(), "Welcome Spongie to the server!");

        Text welcome = findText(text, "Welcome");
        assertEquals(welcome.getColor(), TextColors.GREEN);
        assertEquals(welcome.getShiftClickAction().get(), insertText("Welcome Spongie!"));

        Text spongie = findText(text, "Spongie");
        assertEquals(spongie.getColor(), TextColors.YELLOW);
        assertEquals(spongie.getShiftClickAction().get(), insertText("Welcome Spongie!"));

        Text server = findText(text, "server");
        assertEquals(server.getColor(), TextColors.GREEN);
        assertEquals(server.getShiftClickAction().get(), insertText("Welcome Spongie!"));
    }

    private static Text findText(Text root, String text) {
        for (Text t : root.withChildren()) {
            if (t instanceof LiteralText && ((LiteralText) t).getContent().contains(text)) {
                return t;
            }
        }

        Assertions.fail("No text with content '" + text + "' found");
        throw new AssertionError(); // Should never happen
    }

}
