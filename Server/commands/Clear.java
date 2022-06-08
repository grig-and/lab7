package commands;

import content.Movie;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command clear class
 */
public class Clear implements Commandable {
    CollectionManager collection;
    final public static String description = "очистить коллекцию";

    /**
     * Constructor of clear command
     *
     * @param collection CollectionManager instance
     */
    public Clear(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        if (collection.clear(req.getLogin())) {
            return "Успешно очищено";
        } else {
            return "Не очищено";
        }
    }

    public String getDescription() {
        return description;
    }
}
