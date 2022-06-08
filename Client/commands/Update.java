package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command update class
 */
public class Update extends Commandable {
    final public static String name = "update";
    final public static String description = "обновить значение элемента коллекции, id которого равен заданному";

    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {
        long id;
        try {
            id = Long.parseLong(arg);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Эта команда требует аргумент: id");
        }
        return new Request(name, arg, Movie.prompt());
    }

    public String getDescription() {
        return description;
    }
}
