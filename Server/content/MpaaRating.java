package content;

import exceptions.InvalidParameterException;
import util.IOManager;

/**
 * Mpaa rating enum
 */
public enum MpaaRating {
    /**
     * PG_13 rating
     */
    PG_13,
    /**
     * R rating
     */
    R,
    /**
     * NC_17 rating
     */
    NC_17;

    /**
     * @return MpaaRating from user input
     */
    public static MpaaRating prompt() {
        return (MpaaRating) IOManager.enumPrompt(MpaaRating.values(), "Mpaa рейтинг");
    }

    /**
     * @param str string
     * @return MpaaRating
     * @throws InvalidParameterException
     */
    public static MpaaRating parse(String str) throws InvalidParameterException {
        if (str.equals("null")) {
            return null;
        }
        try {
            return MpaaRating.valueOf(str);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Нет такого варианта рейтинга (" + str + ")");
        }

    }
}
