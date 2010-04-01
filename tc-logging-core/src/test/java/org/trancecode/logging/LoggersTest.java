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

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link Loggers}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class LoggersTest
{
    private static final int MANY_TIMES = 100000;

    @Test
    public void formatMessage()
    {
        Assert.assertEquals(Loggers.formatMessage("a {} c", "b").toString(), "a b c");
        Assert.assertEquals(Loggers.formatMessage("{} b c", "a").toString(), "a b c");
        Assert.assertEquals(Loggers.formatMessage("a {} c {}", "b", 'd').toString(), "a b c d");
        Assert.assertEquals(Loggers.formatMessage("1 {} 3", 2).toString(), "1 2 3");
        Assert.assertEquals(Loggers.formatMessage("null {}", (Object) null).toString(), "null null");
        Assert.assertEquals(Loggers.formatMessage("list {}", ImmutableList.of("a", "b")).toString(), "list [a, b]");
        Assert.assertEquals(Loggers.formatMessage("arrays {} {}", new String[] { "a", "b" }, new String[] { "c", "d" })
                .toString(), "arrays [a, b] [c, d]");
    }

    @Test
    public void formatArgument()
    {
        Assert.assertEquals(Loggers.formatArgument(new Exception("message"), "getMessage").toString(), "message");
    }

    @Test
    public void performance1()
    {
        for (int i = 0; i < MANY_TIMES; i++)
        {
            Loggers.formatMessage(" {} ", "a");
        }
    }

    @Test
    public void performance2()
    {
        for (int i = 0; i < MANY_TIMES; i++)
        {
            Loggers.formatMessage(" {} {} ", "a", "b");
        }
    }

    @Test
    public void performance3()
    {
        for (int i = 0; i < MANY_TIMES; i++)
        {
            Loggers.formatMessage(" {} {} {} ", "a", "b", "c");
        }
    }
}
