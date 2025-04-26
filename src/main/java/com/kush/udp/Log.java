package com.kush.udp;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class Log {

    private static final Map<String, Logger> LOGGERS = new HashMap<>();
    private static final String DEFAULT_INSTANCE_IDENTIFIER = "";

    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    static Logger getLogger() {
        return getLogger(DEFAULT_INSTANCE_IDENTIFIER);
    }

    private static Logger getLogger(String identifier) {
        Logger logger = LOGGERS.get(identifier);
        if (logger == null) {
            logger = Logger.getLogger(Log.class.getName() + identifier);
            if (!DEFAULT_INSTANCE_IDENTIFIER.equals(identifier)) {
                logger.setLevel(getLogger().getLevel());
            }
            attachLogFileHandler(logger, "udp-server" + identifier + ".log");
            LOGGERS.put(identifier, logger);
        }
        return logger;
    }

    private static void attachLogFileHandler(Logger logger, String fileName) {
        logger.setUseParentHandlers(false);
        try {
            FileHandler logFileHandler = new FileHandler(fileName);
            logFileHandler.setEncoding(StandardCharsets.UTF_8.toString());
            logFileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(logFileHandler);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot log to file " + fileName, e);
        }
    }
}