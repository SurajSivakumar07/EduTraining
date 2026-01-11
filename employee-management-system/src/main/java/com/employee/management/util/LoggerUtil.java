package com.employee.management.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Utility class for logger configuration and creation.
 */
public class LoggerUtil {

    static {
        try {
            // Load logging configuration
            InputStream configStream = LoggerUtil.class
                    .getClassLoader()
                    .getResourceAsStream("logging.properties");

            if (configStream != null) {
                LogManager.getLogManager().readConfiguration(configStream);
            }
        } catch (IOException e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
        }
    }

    /**
     * Private constructor to prevent instantiation
     */
    private LoggerUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates a logger for the given class
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
