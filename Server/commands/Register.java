package commands;

import exceptions.UserNotFoundException;
import util.Auth;
import util.DataBase;
import util.Request;

/**
 * Command info class
 */
public class Register implements Commandable {
    final public static String description = null;

    @Override
    public String run(Request req) {
        System.out.println(req);
        if (Auth.register(req.getLogin(), req.getPassword())) {
            return "true";
        } else {
            return "false";
        }

    }

    public String getDescription() {
        return description;
    }

}
