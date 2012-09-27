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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.trancecode.base.Preconditions;
import org.trancecode.base.TcArrays;
import org.trancecode.logging.formatter.ArgumentFormatter;
import org.trancecode.logging.macro.MacroRenderer;

/**
 * Utility methods used internally by the logging facade framework.
 * 
 * @author Herve Quiroz
 */
public final class Loggers
{
    private static final Iterable<ArgumentFormatter> ARGUMENT_FORMATTERS = ServiceLoader.load(ArgumentFormatter.class);
    private static final Map<String, MacroRenderer> MACRO_RENDERERS;

    static
    {
        MACRO_RENDERERS = new HashMap<String, MacroRenderer>();
        for (final MacroRenderer renderer : ServiceLoader.load(MacroRenderer.class))
        {
            Preconditions.checkNotNull(renderer.name());
            Preconditions.checkArgument(renderer.name().startsWith("@"), "name = %s", renderer.name());
            MACRO_RENDERERS.put(renderer.name(), renderer);
        }
    }

    private Loggers()
    {
        // No instantiation
    }

    /**
     * Format a given message, replacing some place-holders using the passed
     * arguments.
     * <p>
     * The tc-logging library uses the same log message syntax as <a
     * href="http://www.slf4j.org/">SLF4J</a> but relies on {@code varargs} to
     * pass an arbitrary number of extra arguments:
     * <p>
     * <code>
     * logger.trace("size is {}", size);
     * logger.trace("a = {} ; b = {} ; c = {} ; d = {}", a, b, c, d);
     * </code>
     * <p>
     * To actually print a curly brace (<code>}</code>), you will have to escape
     * it using a back-slash:
     * <p>
     * <code>
     * logger.trace("this is a curly brace: \\{");
     * </code>
     * <p>
     * Extra formatting hints may be passed within the curly braces. They allow
     * a specific formatting method to be applied to the passed argument:
     * <p>
     * <code>
     * logger.debug("size is {size}", collection); // collection.size()
     * logger.debug("collection is empty? {isEmpty}", collection); // collection.isEmpty()
     * </code>
     * <p>
     * Some keywords in log message are bound to <b>macros</b> rather than a
     * passed argument. They are embedded within curly braces just as regular
     * formatter place-holders but the name starts with a <code>@</code>:
     * <p>
     * <code>
     * public void myMethod()
     * {
     *   logger.trace("in {@method}: i = {}", 4);
     * }
     * </code>
     * <p>
     * will print:
     * <p>
     * <code>
     * in myMethod(): i = 4
     * </code>
     * 
     * @see #formatArgument(Object, String)
     * @see ArgumentFormatter
     * @see #formatMacro(String)
     * @see MacroRenderer
     */
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
            else if (TcArrays.isArray(array[i]))
            {
                formattedArray[i] = formatArray((Object[]) array[i]);
            }
        }

        return Arrays.toString(formattedArray);
    }

    /**
     * Returns the specified argument formatted using the passed method.
     * <p>
     * The <code>method</code> parameter here does not necessarily refer to Java
     * {@link Method}.
     * <p>
     * All registered {@link ArgumentFormatter} instances are queried until one
     * of them supports the argument class and specified method.
     * 
     * @see ArgumentFormatter
     */
    public static Object formatArgument(final Object argument, final String method)
    {
        Preconditions.checkNotNull(method);

        if (argument == null)
        {
            return "null";
        }

        if (method.isEmpty())
        {
            if (TcArrays.isArray(argument))
            {
                return formatArray((Object[]) argument);
            }

            return argument;
        }

        for (final ArgumentFormatter argumentFormatter : ARGUMENT_FORMATTERS)
        {
            final Object formattedArgument = argumentFormatter.formatArgument(argument, method);
            if (formattedArgument != null)
            {
                return formattedArgument;
            }
        }
        throw new UnsupportedOperationException("class = " + argument.getClass() + " ; method = " + method
                + "\nformatters = " + ARGUMENT_FORMATTERS);
    }

    /**
     * Renders the specified macro as an {@link Object} that can be used as an
     * argument in a log message.
     * 
     * @see MacroRenderer
     */
    public static Object formatMacro(final String macro)
    {
        Preconditions.checkNotNull(macro);

        final MacroRenderer macroRenderer = MACRO_RENDERERS.get(macro);
        if (macroRenderer != null)
        {
            return macroRenderer.get();
        }

        throw new UnsupportedOperationException("macro = " + macro);
    }
}
