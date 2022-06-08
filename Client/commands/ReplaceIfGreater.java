package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command replace_if_greater class
 */
public class ReplaceIfGreater extends Commandable{
    final public static String name = "replace_if_greater";
    final public static String description = "заменить значение по ключу, если новое значение больше старого";

    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {

        if (arg == null) {
            throw new InvalidArgumentException("Эта команда требует аргумент: ключ элемента коллекции");
        }

        return new Request(name, arg, Movie.prompt());
    }

    public String getDescription() {
        return description;
    }
}
