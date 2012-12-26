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
package org.trancecode.logging.simple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.trancecode.base.Longs;
import org.trancecode.base.Preconditions;
import org.trancecode.logging.spi.DelegateLogger;
import org.trancecode.logging.spi.LoggerLevel;
import org.trancecode.logging.spi.LoggerManager;

/**
 * @author Herve Quiroz
 */
public final class SimpleLoggerManager extends LoggerManager
{
    public static final String DISABLE_OUTPUT_DIRECTORY = "<none>";
    public static final String PROPERTY_OUTPUT_DIRECTORY = "logging.output.directory";
    public static final String PROPERTY_LEVEL = "logging.level";

    private static final String LINE_DELIMITER = "::";

    private static final File OUTPUT_DIRECTORY;
    private static final LoggerLevel LEVEL;
    private static final ExecutorService OUTPUT_EXECUTOR = Executors.newSingleThreadExecutor(new ThreadFactory()
    {
        @Override
        public Thread newThread(final Runnable r)
        {
            final Thread thread = new Thread(r, SimpleLoggerManager.class.getSimpleName() + ".output");
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
    });

    private static String logFileNamePrefix = ManagementFactory.getRuntimeMXBean().getName();
    private static PrintStream consoleDestination = System.err;

    private static PrintStream fileDestination = null;
    private static int currentFileDayOfTheWeek = 0;
    private static volatile long eventId = 0;

    static
    {
        final String outputDirectoryPath = System.getProperty(PROPERTY_OUTPUT_DIRECTORY,
                System.getProperty("java.io.tmpdir"));
        if (outputDirectoryPath.equals(DISABLE_OUTPUT_DIRECTORY))
        {
            OUTPUT_DIRECTORY = null;
        }
        else
        {
            OUTPUT_DIRECTORY = new File(outputDirectoryPath).getAbsoluteFile();
            if (!OUTPUT_DIRECTORY.exists() && !OUTPUT_DIRECTORY.mkdirs())
            {
                throw new IllegalStateException("Cannot create directory: " + OUTPUT_DIRECTORY);
            }
            if (!OUTPUT_DIRECTORY.isDirectory())
            {
                throw new IllegalStateException("Not a directory: " + OUTPUT_DIRECTORY);
            }
            if (!OUTPUT_DIRECTORY.canWrite())
            {
                throw new IllegalStateException("Cannot write to directory: " + OUTPUT_DIRECTORY);
            }
        }

        final String levelName = System.getProperty(PROPERTY_LEVEL, LoggerLevel.INFO.name());
        LEVEL = LoggerLevel.valueOf(levelName);

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                OUTPUT_EXECUTOR.shutdown();
                try
                {
                    fileDestination.close();
                }
                catch (final Throwable t)
                {
                    t.printStackTrace(consoleDestination);
                }
            }
        });
    }

    private static File getNewLogFile(final String fileNamePrefix)
    {
        final File newLogFile = new File(OUTPUT_DIRECTORY, fileNamePrefix + "."
                + Longs.toHexString(System.currentTimeMillis()) + ".log").getAbsoluteFile();
        if (!newLogFile.exists())
        {
            return newLogFile;
        }

        try
        {
            Thread.sleep(1);
        }
        catch (final InterruptedException e)
        {
            // Ignore
        }
        return getNewLogFile(fileNamePrefix);
    }

    private static PrintStream getFileDestination(final Calendar time)
    {
        final int dayOfTheWeek = time.get(Calendar.DAY_OF_WEEK);
        if (dayOfTheWeek != currentFileDayOfTheWeek)
        {
            final File newLogFile = getNewLogFile(logFileNamePrefix + "."
                    + new SimpleDateFormat("yyyy-MM-dd").format(time.getTime()));
            assert !newLogFile.exists() : newLogFile;
            try
            {
                fileDestination = new PrintStream(new FileOutputStream(newLogFile, true));
            }
            catch (final FileNotFoundException e)
            {
                throw new IllegalStateException("cannot write to new log file: " + newLogFile, e);
            }
            currentFileDayOfTheWeek = dayOfTheWeek;
        }
        return fileDestination;
    }

    public static void setDestination(final PrintStream destination)
    {
        SimpleLoggerManager.consoleDestination = Preconditions.checkNotNull(destination);
    }

    public static void setLogFileNamePrefix(final String logFileNamePrefix)
    {
        SimpleLoggerManager.logFileNamePrefix = logFileNamePrefix;
        // Reset the current log file in case it was already setup
        fileDestination = null;
    }

    private static long nextEventId()
    {
        if (eventId == Long.MAX_VALUE)
        {
            eventId = 0;
        }
        return ++eventId;
    }

    private static void log(final String loggerName, final LoggerLevel level, final Object message)
    {
        final long time = System.currentTimeMillis();
        if (OUTPUT_DIRECTORY != null)
        {
            OUTPUT_EXECUTOR.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    final Calendar date = Calendar.getInstance();
                    date.setTimeInMillis(time);
                    final StringBuilder linePrefix = new StringBuilder();
                    linePrefix.append(Longs.toHexString(nextEventId()));
                    linePrefix.append(LINE_DELIMITER);
                    linePrefix.append(level.toRightPaddedString(5, " "));
                    linePrefix.append(LINE_DELIMITER);
                    linePrefix.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date.getTime()));
                    linePrefix.append(LINE_DELIMITER);
                    linePrefix.append(loggerName);
                    linePrefix.append(LINE_DELIMITER);

                    for (final StringTokenizer t = new StringTokenizer(message.toString(), "\n"); t.hasMoreTokens();)
                    {
                        final String line = linePrefix + t.nextToken();
                        getFileDestination(date).println(line);
                    }
                }
            });
        }
        consoleDestination.println(message);
    }

    @Override
    public DelegateLogger getDelegateLogger(final String name)
    {
        return new DelegateLogger()
        {
            @Override
            public String loggerName()
            {
                return name;
            }

            @Override
            public void log(final LoggerLevel level, final Object message)
            {
                SimpleLoggerManager.log(name, level, message);
            }

            @Override
            public boolean isLevelEnabled(final LoggerLevel level)
            {
                return level.compareTo(LEVEL) >= 0;
            }

            @Override
            public DelegateLogger getChild(final String childName)
            {
                return getDelegateLogger(name + "." + childName);
            }

            @Override
            public void error(final Throwable t)
            {
                t.printStackTrace(consoleDestination);
            }
        };
    }
}
