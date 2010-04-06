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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * @author Herve Quiroz
 */
public final class Loggers
{
    private static final Iterable<ArgumentFormatter> argumentFormatters = ImmutableList.copyOf(ServiceLoader
            .load(ArgumentFormatter.class));
    private static final Map<String, MacroRenderer> macroRenderers;

    static
    {
        final Map<String, MacroRenderer> renderers = Maps.newHashMap();
        for (final MacroRenderer renderer : ServiceLoader.load(MacroRenderer.class))
        {
            Preconditions.checkNotNull(renderer.name());
            Preconditions.checkArgument(renderer.name().startsWith("@"), "name = %s", renderer.name());
            renderers.put(renderer.name(), renderer);
        }

        macroRenderers = ImmutableMap.copyOf(renderers);
    }

    private Loggers()
    {
        // No instantiation
    }

    public static Object formatMessage(final String message, final Object... args)
    {
        if (message.isEmpty())
        {
            return message;
        }

        final StringBuilder formattedMessage = new StringBuilder();
        int alreadyProcessed = 0;
        int argumentRank = 0;
        for (int i = 0; i < message.length(); i++)
        {
            final int open = message.indexOf('{', alreadyProcessed);
            if (open < 0)
            {
                formattedMessage.append(message.substring(alreadyProcessed));
                return formattedMessage;
            }

            if (open > 0 && message.charAt(open - 1) == '\\')
            {
                formattedMessage.append(message.substring(alreadyProcessed, open - 1)).append('{');
                alreadyProcessed = open + 1;
                continue;
            }

            final int close = message.indexOf('}', open);
            Preconditions.checkArgument(close >= 0, "message is missing closing brace: %s", message);
            final String method = message.substring(open + 1, close);
            formattedMessage.append(message.substring(alreadyProcessed, open));
            if (method.startsWith("@"))
            {
                formattedMessage.append(formatMacro(method));
            }
            else
            {
                formattedMessage.append(formatArgument(args[argumentRank], method));
                argumentRank++;
            }
            alreadyProcessed = close + 1;
        }

        return formattedMessage;
    }

    private static final Class<?> OBJECT_ARRAY_CLASS = new Object[0].getClass();

    protected static boolean isArray(final Object object)
    {
        return OBJECT_ARRAY_CLASS.isAssignableFrom(object.getClass());
    }

    private static Object formatArray(final Object[] array)
    {
        final Object[] formattedArray = new Object[array.length];
        System.arraycopy(array, 0, formattedArray, 0, array.length);

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == null)
            {
                formattedArray[i] = "null";
            }
            else if (isArray(array[i]))
            {
                formattedArray[i] = formatArray((Object[]) array[i]);
            }
        }

        return Arrays.toString(formattedArray);
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
                return formatArray((Object[]) argument);
            }

            return argument;
        }

        final Iterable<Object> formattedArguments = Iterables.transform(argumentFormatters,
                new Function<ArgumentFormatter, Object>()
                {
                    @Override
                    public Object apply(final ArgumentFormatter argumentFormatter)
                    {
                        return argumentFormatter.formatArgument(argument, method);
                    }
                });

        try
        {
            return Iterables.find(formattedArguments, Predicates.notNull());
        }
        catch (final NoSuchElementException e)
        {
            throw new UnsupportedOperationException("class = " + argument.getClass() + " ; method = " + method
                    + "\nformatters = " + argumentFormatters, e);
        }
    }

    public static Object formatMacro(final String macro)
    {
        Preconditions.checkNotNull(macro);

        final MacroRenderer macroRenderer = macroRenderers.get(macro);
        if (macroRenderer != null)
        {
            return macroRenderer.get();
        }

        throw new UnsupportedOperationException("macro = " + macro);
    }
}
