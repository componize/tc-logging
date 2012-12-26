/*
 * Copyright 2012 Herve Quiroz
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
package org.trancecode.base;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link Longs}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class LongsTest
{
    @Test
    public void toHexString()
    {
        Assert.assertEquals(Longs.toHexString(0), "0000000000000000");
        Assert.assertEquals(Longs.toHexString(0), "0000000000000000");
        Assert.assertEquals(Longs.toHexString(Integer.MAX_VALUE), "000000007fffffff");
        Assert.assertEquals(Longs.toHexString(Long.MAX_VALUE), "7fffffffffffffff");
    }
}
