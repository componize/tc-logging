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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.trancecode.logging.ArgumentFormatter;

/**
 * @author Herve Quiroz
 */
public final class FileArgumentFormatter implements ArgumentFormatter
{
    private static final Map<String, Function<File, Object>> FUNCTIONS;

    static
    {
        final Map<String, Function<File, Object>> functions = Maps.newHashMap();
        functions.put("canExecute", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.canExecute();
            }
        });
        functions.put("canRead", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.canRead();
            }
        });
        functions.put("canWrite", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.canWrite();
            }
        });
        functions.put("content", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                try
                {
                    return FileUtils.readFileToString(file);
                }
                catch (final IOException e)
                {
                    return "<null>";
                }
            }
        });
        functions.put("exists", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.exists();
            }
        });
        functions.put("length", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.length();
            }
        });
        functions.put("uri", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.toURI();
            }
        });
        functions.put("absolutePath", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
            {
                return file.getAbsolutePath();
            }
        });
        functions.put("canonicalPath", new Function<File, Object>()
        {
            @Override
            public Object apply(final File file)
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
        });

        FUNCTIONS = ImmutableMap.copyOf(functions);
    }

    @Override
    public Object formatArgument(final Object argument, final String method)
    {
        if (argument instanceof File)
        {
            final Function<File, Object> function = FUNCTIONS.get(method);
            if (function != null)
            {
                final File file = (File) argument;
                return function.apply(file);
            }
        }

        return null;
    }
}
