package commands;

import content.Movie;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command info class
 */
public class Info implements Commandable {
    CollectionManager collection;
    final public static String description = "вывести в стандартный поток вывода информацию о коллекции";

    /**
     * Constructor of info command
     *
     * @param collection CollectionManager instance
     */
    public Info(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return collection.getInfo();
    }

    public String getDescription() {
        return description;
    }
}
