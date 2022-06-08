package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * IO Manager
 */
public class IOManager {
    public static void out(String str) {
        System.out.print(str);
    }
    private static final Logger log = LogManager.getLogger();
    private static BufferedReader file;

    public static void setFileReader(BufferedReader reader) {
        file = reader;
    }

    /**
     * @return array of user entered arguments
     */
    public static String[] promptArgs() throws IOException {
        String line = file.readLine();
        return parseArgs(line);
    }

    /**
     * @param line line for parse
     * @return array of arguments
     */
    public static String[] parseArgs(String line) {
        return line.trim().split(" ");
    }

    /**
     * @param msg message for show
     * @return parameter
     */
    public static String prompt(String msg) {
        String line = null;
        System.out.println("prompt" + msg);
        try {
            line = file.readLine();
            System.out.println("line"+line);
        } catch (NoSuchElementException e) {
            System.out.println("Нажат Ctrl+D - выхожу из программы");
            System.exit(0);
            return null;
        } catch (IOException e) {
        }
        System.out.println("line"+line);
        return line.trim();
    }

    /**
     * @param enumValues enum values
     * @param msg        message
     * @return Enum
     */
    public static Enum enumPrompt(Enum[] enumValues, String msg) {
        return enumPrompt(enumValues, msg, "");
    }

    /**
     * @param enumValues enum values
     * @param msg        message
     * @param prefix     prefix
     * @return Enum
     */
    public static Enum enumPrompt(Enum[] enumValues, String msg, String prefix) {
        String options = "";
        for (Enum opt : enumValues) {
            options += prefix + " " + (opt.ordinal() + 1) + ". " + opt + "\n";
        }

        Enum en;

        try {
            String str = IOManager.prompt(prefix + msg + ": \n" +
                    options +
                    prefix + "Введите номер варианта: ");
            if (str.isEmpty()) {
                return null;
            }
            en = enumValues[Integer.parseInt(str) - 1];
        } catch (IndexOutOfBoundsException e) {
            out(prefix + "Введите существующий номер варианта");
            en = enumPrompt(enumValues, msg, " ");
        } catch (NumberFormatException e) {
            out(prefix + "Номер варианта - это число");
            en = enumPrompt(enumValues, msg, " ");
        }
        return en;
    }
}
