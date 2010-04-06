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
package org.trancecode.logging.nul;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.Logger;
import org.trancecode.logging.LoggerManager;

/**
 * Tests for {@link NullLoggerManager}.
 * 
 * @author Herve Quiroz
 */
public final class NullLoggerManagerTest
{
    @Test
    public void getLoggerManager()
    {
        Assert.assertEquals(LoggerManager.getLoggerManager().getClass(), NullLoggerManager.class);
    }

    @Test
    public void getLogger()
    {
        Logger.getLogger(NullLoggerManagerTest.class);
    }

    @Test
    public void log()
    {
        final Logger logger = Logger.getLogger(NullLoggerManagerTest.class);
        logger.trace("trace {}", 123);
        logger.debug("debug {}", 123);
        logger.info("info {}", 123);
        logger.warn("warn {}", 123);
        logger.error("error {}", 123);
        logger.fatal("fatal {}", 123);
    }
}
