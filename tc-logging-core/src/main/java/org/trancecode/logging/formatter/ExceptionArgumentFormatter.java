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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Herve Quiroz
 */
public final class ExceptionArgumentFormatter implements ArgumentFormatter
{
    @Override
    public Object formatArgument(final Object argument, final String method)
    {
        if (argument instanceof Throwable)
        {
            if (method.equals("message"))
            {
                return ((Throwable) argument).getMessage();
            }

            if (method.equals("stackTrace"))
            {
                final StringWriter writer = new StringWriter();
                final PrintWriter out = new PrintWriter(writer);
                try
                {
                    ((Throwable) argument).printStackTrace(out);
                }
                finally
                {
                    out.close();
                }

                return writer;
            }
        }

        return null;
    }
}
