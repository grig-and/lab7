package commands;

import content.Movie;
import content.MovieGenre;
import exceptions.InvalidArgumentException;
import exceptions.InvalidParameterException;
import util.Request;

/**
 * Command filter_greater_than_genre class
 */
public class FilterGreaterThanGenre extends Commandable {
    final public static String name = "filter_greater_than_genre";
    final public static String description = "вывести элементы, значение поля genre которых больше заданного";

    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {
        if (arg == null) {
            throw new InvalidArgumentException("Необходим параметр - жанр");
        }

        try {
            MovieGenre.parse(arg);
        } catch (InvalidParameterException e) {
            throw new InvalidArgumentException("Нет такого жанра");
        }

        return new Request(name, arg);
    }

    public String getDescription() {
        return description;
    }
}
