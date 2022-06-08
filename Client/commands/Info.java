package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command info class
 */
public class Info extends Commandable {
    final public static String name = "info";
    final public static String description = "вывести в стандартный поток вывода информацию о коллекции";

    @Override
    public Request getRequest(String arg) {
        return new Request(name);
    }

    public String getDescription() {
        return description;
    }
}
