/*
 * Copyright 2010 TranceCode
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
package org.trancecode.logging;

import com.google.common.base.Preconditions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Herve Quiroz
 */
public final class Loggers
{
    private Loggers()
    {
        // No instantiation
    }

    public static Object formatMessage(final String message, final Object... args)
    {
        return formatMessage(message, 0, args);
    }

    private static Object formatMessage(final String message, final int current, final Object... args)
    {
        if (!message.matches(".*\\{.*\\}.*"))
        {
            return message;
        }

        final String[] parts1 = message.split("\\{", 2);
        final String beforeTag = parts1[0];
        final String[] parts2 = parts1[1].split("\\}", 2);
        final String tag = parts2[0];
        final String afterTag = parts2[1];
        final StringBuilder builder = new StringBuilder();
        builder.append(beforeTag);
        builder.append(formatArgument(args[current], tag));
        builder.append(formatMessage(afterTag, current + 1, args));

        return builder;
    }

    private static final Class<?> OBJECT_ARRAY_CLASS = new Object[0].getClass();

    private static boolean isArray(final Object object)
    {
        return OBJECT_ARRAY_CLASS.isAssignableFrom(object.getClass());
    }

    public static Object formatArgument(final Object argument, final String method)
    {
        Preconditions.checkNotNull(method);

        if (argument == null)
        {
            return "null";
        }

        if (method.isEmpty())
        {
            if (isArray(argument))
            {
                // TODO handle deep array formatting
                return Arrays.toString((Object[]) argument);
            }

            return argument;
        }

        if (argument instanceof Throwable && method.equals("getMessage"))
        {
            return ((Throwable) argument).getMessage();
        }

        if (argument instanceof Throwable && method.equals("getStackTrace"))
        {
            final StringWriter writer = new StringWriter();
            final PrintWriter out = new PrintWriter(writer);
            try
            {
                ((Throwable) argument).printStackTrace(out);
            }
            finally
            {
                out.close();
            }

            return writer;
        }

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

        // TODO generic method invocation

        throw new UnsupportedOperationException("class = " + argument.getClass() + " ; method = " + method);
    }
}
