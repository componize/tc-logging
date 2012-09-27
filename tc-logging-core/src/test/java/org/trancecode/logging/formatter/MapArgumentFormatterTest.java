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

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.spi.Loggers;

/**
 * Tests for {@link MapArgumentFormatter}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class MapArgumentFormatterTest
{
    @Test
    public void formatArgument()
    {
        final Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        Assert.assertEquals(Loggers.formatArgument(map, "size").toString(), "3");
        Assert.assertEquals(Loggers.formatArgument(map, "isEmpty").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(map, "keys").toString(), "[1, 2, 3]");
        Assert.assertEquals(Loggers.formatArgument(map, "values").toString(), "[one, two, three]");
    }
}
