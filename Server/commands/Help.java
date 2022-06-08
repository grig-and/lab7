package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.Request;

import java.util.Map;

/**
 * Command help class
 */
public class Help implements Commandable {
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
    public String run(Request req) throws InvalidArgumentException {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        String resp = "";
        for (String key : commands.keySet()) {
            resp += key + ": " + commands.get(key).getDescription() + "\n";
        }
        return resp;
    }

    public String getDescription() {
        return description;
    }
}
