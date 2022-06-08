package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Log {
    private static Logger log = null;
    private static boolean isInited = false;

    public Log() {
        if (isInited) return;
        isInited = true;
        if (Files.isWritable(Paths.get("./"))) {
            System.out.println("Пишем логи в ./lab6.log");
            System.setProperty("log4j.saveDirectory", "./");
        } else if (Files.isWritable(Paths.get("/var/log"))) {
            System.out.println("Пишем логи в /var/log/s335080/lab6.log");
            System.setProperty("log4j.saveDirectory", "/var/log/s335080");
        } else if (Files.isWritable(Paths.get("/tmp"))) {
            System.out.println("Пишем логи в /tmp/s335080/lab6.log");
            System.setProperty("log4j.saveDirectory", "/tmp/s335080");
        }
        log = LogManager.getLogger();
    }

    public static Logger getLog() {
        return log;
    }

    public void info(String msg) {
        if (log != null) {
            log.info(msg);
        } else {
            System.out.println(msg);
        }
    }

    public void error(String msg) {
        if (log != null) {
            log.error(msg);
        } else {
            System.out.println(msg);
        }
    }
}
