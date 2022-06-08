package commands;

import content.Movie;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command print_ascending class
 */
public class PrintAscending implements Commandable {
    CollectionManager collection;
    final public static String description = "вывести элементы коллекции в порядке возрастания";

    /**
     * Constructor of print_ascending command
     *
     * @param collection CollectionManager instance
     */
    public PrintAscending(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return collection.getAscending();
    }

    public String getDescription() {
        return description;
    }
}
