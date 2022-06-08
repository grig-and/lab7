package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command replace_if_greater class
 */
public class ReplaceIfGreater implements Commandable {
    CollectionManager collection;

    final public static String description = "заменить значение по ключу, если новое значение больше старого";

    /**
     * Constructor of replace_if_greater command
     *
     * @param collection CollectionManager instance
     */
    public ReplaceIfGreater(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) throws InvalidArgumentException {

        if (req.getArg() == null) {
            throw new InvalidArgumentException("Эта команда требует аргумент: ключ элемента коллекции");
        }
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        if (!collection.contains(req.getArg())) {
            throw new InvalidArgumentException("Элемент с таким ключом не существует");
        }

        if (collection.replaceIfGreater(req.getArg(), req.getObj(), req.getLogin())) {
            return "Заменено";
        } else {
            return "Не заменено";
        }
    }

    public String getDescription() {
        return description;
    }
}
