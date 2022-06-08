package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command remove_greater class
 */
public class RemoveGreater extends Commandable {
    final public static String name = "remove_greater";
    final public static String description = "удалить из коллекции все элементы, превышающие заданный";


    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {
        return new Request(name, null, Movie.prompt());
    }

    public String getDescription() {
        return description;
    }
}
