package util;

import commands.*;
import exceptions.DBError;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Command manager class
 */
public class CommandManager {
    private Map<String, Commandable> commands = new HashMap<>();
    private static Log log = new Log();
    Telegram tg = new Telegram();

    public CommandManager(CollectionManager collectionManager) {
        commands.put("info", new Info(collectionManager));
        commands.put("show", new Show(collectionManager));
        commands.put("insert", new Insert(collectionManager));
        commands.put("update", new Update(collectionManager));
        commands.put("remove_key", new RemoveKey(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("exit", new Exit(collectionManager));
        commands.put("remove_greater", new RemoveGreater(collectionManager));
        commands.put("replace_if_greater", new ReplaceIfGreater(collectionManager));
        commands.put("remove_greater_key", new RemoveGreaterKey(collectionManager));
        commands.put("sum_of_oscars_count", new SumOfOscarsCount(collectionManager));
        commands.put("filter_greater_than_genre", new FilterGreaterThanGenre(collectionManager));
        commands.put("print_ascending", new PrintAscending(collectionManager));
        commands.put("help", new Help(commands));
        commands.put("check_user", new CheckUser());
        commands.put("login", new Login());
        commands.put("register", new Register());

    }

    /**
     * Runs command
     *
     * @param args arguments for run command
     */
    public String run(String[] args) {
        try {
            return commands.get(args[0]).run(new Request(args.length > 1 ? args[1] : null));
        } catch (NullPointerException e) {
            if (!args[0].isEmpty()) {
                log.error("Нет такой команды. Вызовите help для справки по командам.");
                tg.sendMessage("Нет такой команды. Вызовите help для справки по командам.");
            }
            return null;
        } catch (InvalidArgumentException e) {
            System.out.println(e.getMessage());
            return "\u001B[31m" + e.getMessage() + "\u001B[0m";
        } catch (DBError e) {
            return "\u001B[31m" + e.getMessage() + "\u001B[0m";
        }
    }

    public String runPrepared(Request req) {
        try {
            return commands.get(req.getCommand()).run(req);
        } catch (NullPointerException e) {
            return null;
        } catch (InvalidArgumentException e) {
            return "\u001B[31m" + e.getMessage() + "\u001B[0m";
        } catch (DBError e) {
            return "\u001B[31m" + e.getMessage() + "\u001B[0m";
        }
    }
}
