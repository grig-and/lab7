package commands;

import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command remove_greater_key class
 */
public class RemoveGreaterKey extends Commandable {
    final public static String name = "remove_greater_key";
    final public static String description = "удалить из коллекции все элементы, ключ которых превышает заданный";

    @Override
    public Request getRequest(String arg) throws InvalidArgumentException {
        if (arg == null) {
            throw new InvalidArgumentException("Эта команда требует аргумент: ключ элемента коллекции");
        }

        return new Request(name, arg);
    }
    public String getDescription() {
        return description;
    }
}
