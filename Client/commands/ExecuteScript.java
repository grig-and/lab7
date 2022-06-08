package commands;


import exceptions.InvalidArgumentException;
import exceptions.NetException;
import util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Command execute_script class
 */
public class ExecuteScript extends Commandable {
    final public static String name = "execute_script";
    final public static String description = "считать и исполнить скрипт из указанного файла";
    private CommandManager cm;
    private static HashSet<String> history = new HashSet<>();

    public ExecuteScript(CommandManager cm) {
        this.cm = cm;
    }

    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {
        if (arg == null) throw new InvalidArgumentException("Необходим аргумент - путь");


        try {
            if (history.contains(arg)) {
                new InvalidArgumentException("Обнаружен цикл");
            }
            history.add(arg);
            BufferedReader reader = Files.newBufferedReader(Paths.get(arg), StandardCharsets.UTF_8);
            IO.setFileReader(reader);
            String line;
            long i = 0;

            while ((line = reader.readLine()) != null) {
                IO.setIsFileMode(true);
                Request req = cm.run(IO.parseArgs(line));
                String res = NetManager.get().exchange(req);
                System.out.println(res);

                if (res == null) {
                    IO.setIsFileMode(false);
                    throw new InvalidArgumentException("Неверная команда на строке " + i + ": " + line);
                }
                i += 1;
            }
            IO.setIsFileMode(false);

        } catch (InvalidPathException e) {
            throw new InvalidArgumentException("Путь содержит недопустимые символы");
        } catch (IOException e) {
            throw new InvalidArgumentException("Ошибка чтения");
        } catch (NetException | ClassNotFoundException e) {
            throw new InvalidArgumentException("Ошибка сети");
        }

        return null;
    }


    public String getDescription() {
        return description;
    }
}
