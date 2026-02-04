package server.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for logging server events.
 * Logs to console and optionally to a log file.
 * RFC Section 9.3: Log or handle internal errors without exposing sensitive details to client.
 */
public class Logger {
    private static PrintWriter logFile;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void initialize(String logFilePath) {
        if (logFilePath != null && !logFilePath.isEmpty()) {
            try {
                File f = new File(logFilePath);
                logFile = new PrintWriter(new FileWriter(f, true), true);
            } catch (IOException e) {
                System.err.println("Logger: could not open file " + logFilePath + ": " + e.getMessage());
            }
        }
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warning(String message) {
        log("WARNING", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    private static void log(String level, String message) {
        String ts = getTimestamp();
        String line = "[" + ts + "] " + level + " — " + message;
        System.out.println(line);
        if (logFile != null) {
            logFile.println(line);
        }
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

    public static void close() {
        if (logFile != null) {
            logFile.close();
            logFile = null;
        }
    }
}
