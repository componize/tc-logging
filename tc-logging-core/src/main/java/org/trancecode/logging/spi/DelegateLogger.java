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

import org.trancecode.logging.Logger;

/**
 * Provides the internal logic of a {@link Logger} facade.
 * <p>
 * Should be implemented by service providers along with {@link LoggerManager}.
 * 
 * @author Herve Quiroz
 * @see LoggerManager#getDelegateLogger(String)
 */
public interface DelegateLogger
{
    /**
     * Returns the name of the underlying logger or logging channel.
     */
    String loggerName();

    /**
     * Returns {@code true} if the specified logging level is enabled on the
     * underlying logger or logging channel.
     */
    boolean isLevelEnabled(LoggerLevel level);

    /**
     * Logs a message through the underlying logger or logging channel for the
     * specified level.
     */
    void log(LoggerLevel level, Object message);

    /**
     * Returns a child logger of this logger.
     * <p>
     * The logic here depends on the naming scheme used by the underlying
     * logging mechanism.
     */
    DelegateLogger getChild(String childName);

    /**
     * Report an internal error to the underlying logger or logging channel.
     * <p>
     * This method is used internally by tc-logging to report misuses of the
     * tc-logging framework or tc-logging implementation flaws.
     */
    void error(Throwable t);
}
