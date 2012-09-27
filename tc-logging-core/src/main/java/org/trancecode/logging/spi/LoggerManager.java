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

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Herve Quiroz
 */
public abstract class LoggerManager
{
    private static class LoggerManagerFactory
    {
        private static final LoggerManager INSTANCE;
        static
        {
            final ServiceLoader<LoggerManager> serviceLoader = ServiceLoader.load(LoggerManager.class);
            final Iterator<LoggerManager> loggerManagers = serviceLoader.iterator();
            if (!loggerManagers.hasNext())
            {
                throw new IllegalStateException(String.format("no %s implementation could be found on the classpath",
                        LoggerManager.class.getName()));
            }
            INSTANCE = loggerManagers.next();
            if (loggerManagers.hasNext())
            {
                throw new IllegalStateException(String.format(
                        "more than one %s implementation could be found on the classpath",
                        LoggerManager.class.getName()));
            }
        }
    }

    public static final LoggerManager getLoggerManager()
    {
        return LoggerManagerFactory.INSTANCE;
    }

    public abstract DelegateLogger getDelegateLogger(final String name);

    public DelegateLogger getDelegateLogger(final Class<?> forClass)
    {
        return getDelegateLogger(forClass.getName());
    }
}
