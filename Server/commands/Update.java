package commands;

import content.Movie;
import exceptions.DBError;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.Request;

/**
 * Command update class
 */
public class Update implements Commandable {
    CollectionManager collection;

    final public static String description = "обновить значение элемента коллекции, id которого равен заданному";

    /**
     * Constructor of update command
     *
     * @param collection CollectionManager instance
     */
    public Update(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) throws InvalidArgumentException, DBError {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }
        long id;
        try {
            id = Long.parseLong(req.getArg());
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Эта команда требует аргумент: id");
        }
        if (!collection.containsID(id)) {
            throw new InvalidArgumentException("Элемента с таким id нет");
        }
        collection.update(id, req.getObj(), req.getLogin());
        return "Фильм успешно отредактирован";
    }

    public String getDescription() {
        return description;
    }
}
