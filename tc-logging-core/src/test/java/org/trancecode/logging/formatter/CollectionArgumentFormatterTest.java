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
package org.trancecode.logging.formatter;

import com.google.common.collect.ImmutableList;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.spi.Loggers;

/**
 * Tests for {@link CollectionArgumentFormatter}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class CollectionArgumentFormatterTest
{
    @Test
    public void formatArgument()
    {
        Assert.assertEquals(Loggers.formatArgument(ImmutableList.of(1, 2, 3), "size").toString(), "3");
        Assert.assertEquals(Loggers.formatArgument(ImmutableList.of(1, 2, 3), "isEmpty").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(new Object[] { 1, 2, 3 }, "length").toString(), "3");
    }
}
