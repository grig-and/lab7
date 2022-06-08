package commands;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command show class
 */
public class Show extends Commandable {
    final public static String name = "show";
    final public static String description = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";

    @Override
    public Request getRequest(String arg) {
        return new Request(name);
    }

    public String getDescription() {
        return description;
    }
}
