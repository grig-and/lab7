package commands;

import content.Movie;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command sum_of_oscars_count class
 */
public class SumOfOscarsCount implements Commandable {
    CollectionManager collection;

    final public static String description = "вывести сумму значений поля oscarsCount для всех элементов коллекции";

    /**
     * Constructor of sum_of_oscars_count command
     *
     * @param collection CollectionManager instance
     */
    public SumOfOscarsCount(CollectionManager collection) {
        this.collection = collection;
    }


    @Override
    public String run(Request req) {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return "Сумма оскаров: " + collection.getSumOscars();
    }

    public String getDescription() {
        return description;
    }
}
