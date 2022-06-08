package commands;

import content.Movie;
import exceptions.DBError;
import exceptions.InvalidArgumentException;
import util.Request;

/**
 * Interface for commands
 */
public interface Commandable {
    /**

     * @throws InvalidArgumentException
     */
    String run(Request req) throws InvalidArgumentException, DBError;
    /**
     * @return description for Help command
     */
    String getDescription();
}
