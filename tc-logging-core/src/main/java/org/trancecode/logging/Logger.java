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

/**
 * @author Herve Quiroz
 */
public final class Logger
{
    private final DelegateLogger delegateLogger;

    public static Logger getLogger(final Class<?> forClass)
    {
        return new Logger(LoggerManager.getLoggerManager().getDelegateLogger(forClass));
    }

    public static Logger getLogger(final String name)
    {
        return new Logger(LoggerManager.getLoggerManager().getDelegateLogger(name));
    }

    private Logger(final DelegateLogger delegateLogger)
    {
        this.delegateLogger = Preconditions.checkNotNull(delegateLogger);
    }

    public String name()
    {
        return delegateLogger.loggerName();
    }

    public Logger getChildLogger(final String name)
    {
        return new Logger(delegateLogger.getChild(name));
    }

    private void log(final LoggerLevel level, final String message, final Object... args)
    {
        if (delegateLogger.isLevelEnabled(level))
        {
            try
            {
                delegateLogger.log(level, Loggers.formatMessage(message, args));
            }
            catch (final Exception e)
            {
                delegateLogger.error(e);
            }
        }
    }

    public void trace(final String message, final Object... args)
    {
        log(LoggerLevel.TRACE, message, args);
    }

    public void debug(final String message, final Object... args)
    {
        log(LoggerLevel.DEBUG, message, args);
    }

    public void info(final String message, final Object... args)
    {
        log(LoggerLevel.INFO, message, args);
    }

    public void warn(final String message, final Object... args)
    {
        log(LoggerLevel.WARN, message, args);
    }

    public void error(final String message, final Object... args)
    {
        log(LoggerLevel.ERROR, message, args);
    }

    public void fatal(final String message, final Object... args)
    {
        log(LoggerLevel.FATAL, message, args);
    }
}
