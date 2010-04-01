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
package org.trancecode.logging.log4j;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.trancecode.logging.DelegateLogger;
import org.trancecode.logging.LoggerLevel;
import org.trancecode.logging.LoggerManager;

/**
 * @author Herve Quiroz
 */
public final class Log4jLoggerManager extends LoggerManager
{
    private static final Map<LoggerLevel, Level> LEVELS;

    static
    {
        final Map<LoggerLevel, Level> levels = Maps.newHashMap();
        levels.put(LoggerLevel.TRACE, Level.TRACE);
        levels.put(LoggerLevel.DEBUG, Level.DEBUG);
        levels.put(LoggerLevel.INFO, Level.INFO);
        levels.put(LoggerLevel.WARN, Level.WARN);
        levels.put(LoggerLevel.ERROR, Level.ERROR);
        levels.put(LoggerLevel.FATAL, Level.FATAL);
        LEVELS = ImmutableMap.copyOf(levels);
    }

    @Override
    public DelegateLogger getDelegateLogger(final String name)
    {
        return new Log4jDelegateLogger(name, Logger.getLogger(name));
    }

    private static Level getLevel(final LoggerLevel level)
    {
        return LEVELS.get(Preconditions.checkNotNull(level));
    }

    private static final class Log4jDelegateLogger implements DelegateLogger
    {
        private final String name;
        private final Logger logger;

        public Log4jDelegateLogger(final String name, final Logger logger)
        {
            this.name = Preconditions.checkNotNull(name);
            this.logger = Preconditions.checkNotNull(logger);
        }

        @Override
        public DelegateLogger getChild(final String childName)
        {
            Preconditions.checkNotNull(childName);
            Preconditions.checkArgument(!childName.isEmpty());
            final String childFullName = name + "." + childName;
            return new Log4jDelegateLogger(childFullName, Logger.getLogger(childFullName));
        }

        @Override
        public boolean isLevelEnabled(final LoggerLevel level)
        {
            return logger.isEnabledFor(getLevel(level));
        }

        @Override
        public void log(final LoggerLevel level, final Object message)
        {
            logger.log(getLevel(level), message);
        }

        @Override
        public String loggerName()
        {
            return name;
        }

        @Override
        public void error(final Throwable t)
        {
            logger.error(t.toString(), t);
        }
    }
}
