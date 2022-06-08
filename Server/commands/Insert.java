package commands;

import content.Movie;
import exceptions.DBError;
import exceptions.InvalidArgumentException;
import exceptions.UserNotFoundException;
import util.Auth;
import util.CollectionManager;
import util.DataBase;
import util.Request;

/**
 * Command insert class
 */
public class Insert implements Commandable {
    CollectionManager collection;
    final public static String description = "добавить новый элемент с заданным ключом";

    /**
     * Constructor of insert command
     *
     * @param collection CollectionManager instance
     */
    public Insert(CollectionManager collection) {
        this.collection = collection;
    }

    @Override
    public String run(Request req) throws InvalidArgumentException, DBError {
        try {
            if (!Auth.checkRequest(req)) return "Ошибка авторизации: неверный пароль";
        } catch (UserNotFoundException e) {
            return "Ошибка авторизации: юзер не найден";
        }

        if (req.getArg() == null) {
            throw new InvalidArgumentException("Эта команда требует аргумент: ключ элемента коллекции");
        }
        if (collection.contains(req.getArg())) {
            throw new InvalidArgumentException("Элемент с таким ключом уже существует");
        }
        Movie obj = req.getObj();
        if (obj == null) {
            obj = Movie.prompt();
        }

        if (collection.insert(req.getArg(), obj, req.getLogin())) {
            return "Фильм успешно добавлен";
        } else {
            return "Фильм не добавлен";
        }
    }

    public String getDescription() {
        return description;
    }
}
