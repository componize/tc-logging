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
package org.trancecode.logging;

import org.trancecode.base.Preconditions;
import org.trancecode.logging.spi.DelegateLogger;
import org.trancecode.logging.spi.LoggerLevel;
import org.trancecode.logging.spi.LoggerManager;
import org.trancecode.logging.spi.Loggers;

/**
 * The main logger facade class.
 * 
 * @author Herve Quiroz
 */
public final class Logger
{
    private final DelegateLogger delegateLogger;

    /**
     * Returns a logger for the specified class.
     */
    public static Logger getLogger(final Class<?> forClass)
    {
        return new Logger(LoggerManager.getLoggerManager().getDelegateLogger(forClass));
    }

    /**
     * Returns a logger for the specified logging channel name.
     */
    public static Logger getLogger(final String name)
    {
        return new Logger(LoggerManager.getLoggerManager().getDelegateLogger(name));
    }

    private Logger(final DelegateLogger delegateLogger)
    {
        this.delegateLogger = Preconditions.checkNotNull(delegateLogger);
    }

    /**
     * Returns the name of the logging channel.
     */
    public String name()
    {
        return delegateLogger.loggerName();
    }

    /**
     * Return a child logger of this logging channel.
     */
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

    /**
     * Sends a {@link LoggerLevel#TRACE}-level log event to the underlying
     * logger or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#TRACE}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void trace(final String message, final Object... args)
    {
        log(LoggerLevel.TRACE, message, args);
    }

    /**
     * Sends a {@link LoggerLevel#DEBUG}-level log event to the underlying
     * logger or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#DEBUG}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void debug(final String message, final Object... args)
    {
        log(LoggerLevel.DEBUG, message, args);
    }

    /**
     * Sends a {@link LoggerLevel#INFO}-level log event to the underlying logger
     * or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#INFO}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void info(final String message, final Object... args)
    {
        log(LoggerLevel.INFO, message, args);
    }

    /**
     * Sends a {@link LoggerLevel#WARN}-level log event to the underlying logger
     * or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#WARN}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void warn(final String message, final Object... args)
    {
        log(LoggerLevel.WARN, message, args);
    }

    /**
     * Sends a {@link LoggerLevel#ERROR}-level log event to the underlying
     * logger or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#ERROR}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void error(final String message, final Object... args)
    {
        log(LoggerLevel.ERROR, message, args);
    }

    /**
     * Sends a {@link LoggerLevel#FATAL}-level log event to the underlying
     * logger or logging channel.
     * <p>
     * The message is actually only formatted if the {@link LoggerLevel#FATAL}
     * level is enabled for the underlying logger or logging channel.
     * 
     * @see Loggers#formatMessage(String, Object...)
     */
    public void fatal(final String message, final Object... args)
    {
        log(LoggerLevel.FATAL, message, args);
    }
}
