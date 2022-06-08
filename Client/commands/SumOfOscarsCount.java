package commands;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Command sum_of_oscars_count class
 */
public class SumOfOscarsCount extends Commandable {
    final public static String name = "sum_of_oscars_count";
    final public static String description = "вывести сумму значений поля oscarsCount для всех элементов коллекции";

    @Override
    public Request getRequest(String arg) {
        return new Request(name);
    }

    public String getDescription() {
        return description;
    }
}
