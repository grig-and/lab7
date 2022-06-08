package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command remove_greater_key class
 */
public class RemoveGreaterKey implements Commandable {
    CollectionManager collection;
    final public static String description = "удалить из коллекции все элементы, ключ которых превышает заданный";

    /**
     * Constructor of remove_greater_key command
     *
     * @param collection CollectionManager instance
     */
    public RemoveGreaterKey(CollectionManager collection) {
        this.collection = collection;
    }


    @Override
    public String run(Request req) throws InvalidArgumentException {
        if (req.getArg() == null){
            throw new InvalidArgumentException("Эта команда требует аргумент: ключ элемента коллекции");
        }
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        return "Удалено элементов: " + collection.removeGreaterKey(req.getArg(), req.getLogin());
    }

    public String getDescription() {
        return description;
    }
}
