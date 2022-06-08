package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command print_ascending class
 */
public class PrintAscending extends Commandable {
    final public static String name = "print_ascending";
    final public static String description = "вывести элементы коллекции в порядке возрастания";

    @Override
    public Request getRequest(String arg) {
        return new Request(name);
    }

    public String getDescription() {
        return description;
    }
}
