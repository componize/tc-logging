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
package org.trancecode.logging.formatter;

/**
 * @author Herve Quiroz
 */
public final class StringArgumentFormatter implements ArgumentFormatter
{
    @Override
    public Object formatArgument(final Object argument, final String method)
    {
        if (argument instanceof String)
        {
            if (method.equals("isEmpty"))
            {
                return ((String) argument).isEmpty();
            }

            if (method.equals("length"))
            {
                return ((String) argument).length();
            }

            if (method.equals("trim"))
            {
                return ((String) argument).trim();
            }

            if (method.equals("toLowerCase"))
            {
                return ((String) argument).toLowerCase();
            }

            if (method.equals("toUpperCase"))
            {
                return ((String) argument).toUpperCase();
            }
        }

        return null;
    }
}
