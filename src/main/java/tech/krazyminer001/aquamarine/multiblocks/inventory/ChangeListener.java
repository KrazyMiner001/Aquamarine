/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package tech.krazyminer001.aquamarine.multiblocks.inventory;

import java.util.List;
import java.util.Map;

/**
 * Class to listen for changes to {@link ConfigurableStack}s.
 */
public abstract class ChangeListener {

    /**
     * What the superclass must do when it detects a change.
     */
    protected abstract void onChange();

    /**
     * Checks if an object is valid for this listener.
     * @param token The object to check validity of.
     * @return If the object is valid.
     */
    protected abstract boolean isValid(Object token);

    /**
     * Adds listeners for a list of stacks.
     * @param stacks The list of stacks to add listeners for.
     * @param token The token the listeners must have.
     */
    public void listenAll(List<? extends ConfigurableStack<?, ?>> stacks, Object token) {
        for (var stack : stacks) {
            stack.addListener(this, token);
        }
    }

    /**
     * Notifies a set of listeners about change.
     * @param listeners A map of the listeners to notify and their tokens.
     */
    public static void notify(Map<ChangeListener, Object> listeners) {
        for (var it = listeners.entrySet().iterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getKey().isValid(entry.getValue())) {
                entry.getKey().onChange();
            } else {
                it.remove();
            }
        }
    }
}