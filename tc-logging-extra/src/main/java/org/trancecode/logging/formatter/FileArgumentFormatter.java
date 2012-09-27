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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Herve Quiroz
 */
public final class FileArgumentFormatter implements ArgumentFormatter
{
    @Override
    public Object formatArgument(final Object argument, final String method)
    {
        if (argument instanceof File)
        {
            final File file = (File) argument;
            if ("canExecute".equals(method))
            {
                return file.canExecute();
            }
            if ("canRead".equals(method))
            {
                return file.canRead();
            }
            if ("canWrite".equals(method))
            {
                return file.canWrite();
            }
            if ("content".equals(method))
            {
                FileReader in = null;
                final StringWriter out = new StringWriter();
                try
                {
                    in = new FileReader(file);
                    final char[] buffer = new char[4096];
                    int readBytes = 0;
                    while (-1 != (readBytes = in.read(buffer)))
                    {
                        out.write(buffer, 0, readBytes);
                    }
                }
                catch (final IOException e)
                {
                    return "<null>";
                }
                finally
                {
                    if (in != null)
                    {
                        try
                        {
                            in.close();
                        }
                        catch (final IOException e)
                        {
                            // Ignore
                        }
                    }
                    try
                    {
                        out.close();
                    }
                    catch (final IOException e)
                    {
                        // Ignore
                    }
                }
                return out.toString();
            }
            if ("exists".equals(method))
            {
                return file.exists();
            }
            if ("length".equals(method))
            {
                return file.length();
            }
            if ("uri".equals(method))
            {
                return file.toURI();
            }
            if ("absolutePath".equals(method))
            {
                return file.getAbsolutePath();
            }
            if ("canonicalPath".equals(method))
            {
                try
                {
                    return file.getCanonicalPath();
                }
                catch (final IOException e)
                {
                    return "<null>";
                }
            }
        }

        return null;
    }
}
