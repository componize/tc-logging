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

import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.Logger;
import org.trancecode.logging.spi.LoggerManager;
import org.trancecode.logging.util.Duration;

/**
 * Tests for {@link SimpleLoggerManager}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class SimpleLoggerManagerTest
{
    public static final Logger STATIC_LOGGER = Logger.getLogger();

    @Test
    public void getLoggerManager()
    {
        Assert.assertEquals(LoggerManager.getLoggerManager().getClass(), SimpleLoggerManager.class);
    }

    @Test
    public void getLogger()
    {
        Logger.getLogger(SimpleLoggerManagerTest.class);
    }

    @Test
    public void log()
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);
        logger.trace("trace {}", 123);
        logger.debug("debug {}", 123);
        logger.info("info {}", 123);
        logger.warn("warn {}", 123);
        logger.error("error {}", 123);
        logger.fatal("fatal {}", 123);
    }

    @Test
    public void logMethodMacro()
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);
        logger.info("{@method}");
    }

    @Test
    public void duration() throws Exception
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);

        final Object milliseconds = Duration.of(TimeUnit.MILLISECONDS);
        Thread.sleep(10);
        logger.info("{}", milliseconds);

        final Object seconds = Duration.of(TimeUnit.SECONDS);
        Thread.sleep(10);
        logger.info("{}", seconds);
    }

    public static int someMethod1(final String someStringParameter, final boolean someBooleanParameter)
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);
        logger.methodInvoked(someStringParameter, someBooleanParameter);
        return logger.methodReturned(123);
    }

    public static CharSequence someMethod2(final int someIntParameter)
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);
        logger.methodInvoked(someIntParameter);
        return logger.methodReturned("abc");
    }

    @Test
    public void methodTraces()
    {
        someMethod1("abc", true);
        someMethod2(123);
    }

    @Test
    public void staticLoggerName()
    {
        Assert.assertEquals(STATIC_LOGGER.name(), SimpleLoggerManagerTest.class.getName());
    }

    @Test
    public void multiline()
    {
        final Logger logger = Logger.getLogger(SimpleLoggerManagerTest.class);
        logger.info("one\n two\n  three");
    }
}
