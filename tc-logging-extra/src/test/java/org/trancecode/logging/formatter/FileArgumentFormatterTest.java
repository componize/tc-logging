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

import java.io.File;
import java.io.FileOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.trancecode.logging.spi.Loggers;

/**
 * Tests for {@link FileArgumentFormatter}.
 * 
 * @author Herve Quiroz
 */
@Test
public final class FileArgumentFormatterTest
{
    @Test
    public void format() throws Exception
    {
        final File file = File.createTempFile(getClass().getSimpleName(), "");
        final FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write("abc".getBytes());
        fileOut.close();
        Assert.assertEquals(Loggers.formatArgument(file, "canExecute").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(file, "canRead").toString(), "true");
        Assert.assertEquals(Loggers.formatArgument(file, "canWrite").toString(), "true");
        Assert.assertEquals(Loggers.formatArgument(file, "content").toString(), "abc");
        Assert.assertEquals(Loggers.formatArgument(file, "exists").toString(), "true");
        Assert.assertEquals(Loggers.formatArgument(file, "length").toString(), "3");

        final File invalidFile = new File("/" + Math.random() + "/" + Math.random());
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "canExecute").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "canRead").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "canWrite").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "content").toString(), "<null>");
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "exists").toString(), "false");
        Assert.assertEquals(Loggers.formatArgument(invalidFile, "length").toString(), "0");
    }
}
