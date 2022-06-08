package commands;

import exceptions.UserNotFoundException;
import util.Auth;
import util.DataBase;
import util.Request;

/**
 * Command info class
 */
public class Login implements Commandable {
    final public static String description = null;

    @Override
    public String run(Request req) {
        try {
            if (Auth.checkRequest(req)) {
                return "true";
            } else {
                return "false";
            }
        } catch (UserNotFoundException e) {
            return "User not found";
        }
    }

    public String getDescription() {
        return description;
    }

}
