package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

import java.util.Map;

/**
 * Command help class
 */
public class Help extends Commandable {
    final public static String description = "вывести справку по доступным командам";
    final public static String name = "help";

    /**
     * commands list
     */
    private Map<String, Commandable> commands;

    /**
     * Constructor of help command
     *
     * @param commands commands list
     */
    public Help(Map<String, Commandable> commands) {
        this.commands = commands;
    }

    @Override
    public Request getRequest(String arg) {
        for (String key : commands.keySet()) {
            System.out.println("\u001B[34m" + key + ": " + "\u001B[0m" + commands.get(key).getDescription());
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}
