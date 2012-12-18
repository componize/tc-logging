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
package org.trancecode.logging.simple;

import java.io.PrintStream;

import org.trancecode.base.Preconditions;
import org.trancecode.logging.spi.DelegateLogger;
import org.trancecode.logging.spi.LoggerLevel;
import org.trancecode.logging.spi.LoggerManager;

/**
 * @author Herve Quiroz
 */
public final class SimpleLoggerManager extends LoggerManager
{
    private static PrintStream destination = System.err;

    public static void setDestination(final PrintStream destination)
    {
        SimpleLoggerManager.destination = Preconditions.checkNotNull(destination);
    }

    @Override
    public DelegateLogger getDelegateLogger(final String name)
    {
        return new DelegateLogger()
        {
            @Override
            public String loggerName()
            {
                return name;
            }

            @Override
            public void log(final LoggerLevel level, final Object message)
            {
                destination.println(message);
            }

            @Override
            public boolean isLevelEnabled(final LoggerLevel level)
            {
                return true;
            }

            @Override
            public DelegateLogger getChild(final String childName)
            {
                return getDelegateLogger(name + "." + childName);
            }

            @Override
            public void error(final Throwable t)
            {
                t.printStackTrace(destination);
            }
        };
    }
}
