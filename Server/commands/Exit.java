package commands;

import content.Movie;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command exit class
 */
public class Exit implements Commandable {
    CollectionManager collection;
    final public static String description = "завершить программу";

    public Exit(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}
