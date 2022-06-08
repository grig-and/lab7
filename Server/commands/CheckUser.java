package commands;

import content.Movie;
import exceptions.InvalidArgumentException;
import util.CollectionManager;
import util.DataBase;
import util.Request;

/**
 * Command info class
 */
public class CheckUser implements Commandable {
    final public static String description = null;

    @Override
    public String run(Request req) {
        System.out.println(req);
        if (DataBase.getInstance().getUser(req.getArg()) != null) {
            return "true";
        } else {
            return "false";
        }
    }

    public String getDescription() {
        return description;
    }

}
