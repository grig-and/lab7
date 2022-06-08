package util;

import commands.*;
import exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Command manager class
 */
public class CommandManager {
    private Map<String, Commandable> commands = new HashMap<>();

    public CommandManager() {
        commands.put(Help.name, new Help(commands));
        commands.put(Info.name, new Info());
        commands.put(Show.name, new Show());
        commands.put(Insert.name, new Insert());
        commands.put(Update.name, new Update());
        commands.put(RemoveKey.name, new RemoveKey());
        commands.put(Clear.name, new Clear());
        commands.put(ExecuteScript.name, new ExecuteScript(this));
        commands.put(Exit.name, new Exit());
        commands.put(RemoveGreater.name, new RemoveGreater());
        commands.put(ReplaceIfGreater.name, new ReplaceIfGreater());
        commands.put(RemoveGreaterKey.name, new RemoveGreaterKey());
        commands.put(SumOfOscarsCount.name, new SumOfOscarsCount());
        commands.put(FilterGreaterThanGenre.name, new FilterGreaterThanGenre());
        commands.put(PrintAscending.name, new PrintAscending());
    }

    /**
     * Runs command
     *
     * @param args arguments for run command
     */
    public Request run(String[] args) {
        try {
            return commands.get(args[0]).getRequest(args.length > 1 ? args[1] : null);
        } catch (NullPointerException e) {
            if (!args[0].isEmpty()) {
                IO.error("Нет такой команды. Вызовите help для справки по командам.");
            }
            return null;
        } catch (InvalidArgumentException e) {
            IO.error(e.getMessage());
            return null;
        }
    }

}
