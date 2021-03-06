/*
 * Copyright 2010 Herve Quiroz
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
package org.trancecode.logging.spi;

/**
 * @author Herve Quiroz
 */
public enum LoggerLevel
{
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

    public String toRightPaddedString(final int length, final String padding)
    {
        return toRightPaddedString(toString(), length, padding);
    }

    private static String toRightPaddedString(final String string, final int length, final String padding)
    {
        assert !padding.isEmpty();
        if (string.length() < length)
        {
            return toRightPaddedString(string + padding, length, padding);
        }

        return string;
    }
}
