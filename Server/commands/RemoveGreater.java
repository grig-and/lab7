package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command remove_greater class
 */
public class RemoveGreater implements Commandable {
    CollectionManager collection;
    final public static String description = "удалить из коллекции все элементы, превышающие заданный";

    /**
     * Constructor of remove_greater command
     *
     * @param collection CollectionManager instance
     */
    public RemoveGreater(CollectionManager collection) {
        this.collection = collection;
    }


    @Override
    public String run(Request req) throws InvalidArgumentException {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return "Удалено элементов: " + collection.removeGreater(req.getObj(), req.getLogin());
    }

    public String getDescription() {
        return description;
    }
}
