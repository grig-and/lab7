package content;

import exceptions.InvalidParameterException;
import util.IO;


/**
 * Movie genre enum
 */
public enum MovieGenre {
    /**
     * Comedy genre
     */
    COMEDY,
    /**
     * Adventure genre
     */
    ADVENTURE,
    /**
     * Tragedy genre
     */
    TRAGEDY;

    /**
     * @return MovieGenre from user input
     */
    public static MovieGenre prompt() {
        return (MovieGenre) IO.enumPrompt(MovieGenre.values(), "Жанр");
    }

    /**
     * @param str string for parse
     * @return MovieGenre
     * @throws InvalidParameterException
     */
    public static MovieGenre parse(String str) throws InvalidParameterException {
        if (str.equals("null")) {
            return null;
        }
        try {
            return MovieGenre.valueOf(str);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Нет такого варианта жанра");
        }

    }
}
