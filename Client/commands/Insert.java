package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command insert class
 */
public class Insert extends Commandable {
    final public static String name = "insert";
    final public static String description = "добавить новый элемент с заданным ключом";

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
