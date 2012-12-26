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

import java.util.Collection;

/**
 * @author Herve Quiroz
 */
public final class CollectionArgumentFormatter implements ArgumentFormatter
{
    @Override
    public Object formatArgument(final Object argument, final String method)
    {
        if (argument instanceof Collection<?>)
        {
            final Collection<?> collection = (Collection<?>) argument;
            if (method.equals("size"))
            {
                return collection.size();
            }

            if (method.equals("isEmpty"))
            {
                return collection.isEmpty();
            }
        }

        return null;
    }
}
