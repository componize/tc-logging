/*
 * Copyright 2012 Herve Quiroz
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.trancecode.base;

/**
 * Utility methods related to {@code long} and {@link Long}.
 * 
 * @author Herve Quiroz
 */
public final class Longs
{
    private Longs()
    {
        // No instantiation
    }

    private static final String[] PADDING;

    static
    {
        PADDING = new String[16];
        for (int i = PADDING.length - 1; i >= 0; i--)
        {
            if (i == PADDING.length - 1)
            {
                PADDING[i] = "0";
            }
            else
            {
                PADDING[i] = PADDING[i + 1] + "0";
            }
        }
    }

    private static String getPadding(final int currentSize)
    {
        if (currentSize >= PADDING.length)
        {
            return "";
        }
        return PADDING[currentSize];
    }

    public static String toHexString(final long value)
    {
        final String string = Long.toHexString(value);
        return getPadding(string.length()) + string;
    }
}
