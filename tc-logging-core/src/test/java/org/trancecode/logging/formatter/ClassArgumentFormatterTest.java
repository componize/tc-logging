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
package org.trancecode.logging.formatter;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.spi.Loggers;

/**
 * Tests for {@link ClassArgumentFormatter}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class ClassArgumentFormatterTest
{
    @Test
    public void formatArgument()
    {
        Assert.assertEquals(Loggers.formatArgument("abc", "class").toString(), "class java.lang.String");
        Assert.assertEquals(Loggers.formatArgument("abc", "class.name").toString(), "java.lang.String");
        Assert.assertEquals(Loggers.formatArgument("abc", "class.simpleName").toString(), "String");
        Assert.assertEquals(Loggers.formatArgument("abc", "class.package").toString(), "java.lang");
    }
}
