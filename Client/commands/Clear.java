package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command clear class
 */
public class Clear extends Commandable {
    final public static String name = "clear";
    final public static String description = "очистить коллекцию";

    @Override
    public Request getRequest(String arg) {
        return new Request(name);
    }

    public String getDescription() {
        return description;
    }
}
