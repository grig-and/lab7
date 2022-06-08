package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command remove_key class
 */
public class RemoveKey implements Commandable {
    CollectionManager collection;
    final public static String description = "удалить элемент из коллекции по его ключу";

    /**
     * Constructor of remove_key command
     *
     * @param collection CollectionManager instance
     */
    public RemoveKey(CollectionManager collection) {
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
        if (!collection.contains(req.getArg())) {
            throw new InvalidArgumentException("Элемента с таким ключом не существует");
        }
        collection.removeKey(req.getArg(), req.getLogin());
        return "Успешно удалено";
    }

    public String getDescription() {
        return description;
    }
}
