package content;

import exceptions.InvalidParameterException;
import util.IOManager;


/**
 * Color enum
 */
public enum Color{
    /**
     * green color
     */
    GREEN,
    /**
     * blue color
     */
    BLUE,
    /**
     * yellow color
     */
    YELLOW,
    /**
     * orange color
     */
    ORANGE,
    /**
     * brown color
     */
    BROWN;

    /**
     * Prompt a color from user
     *
     * @return requested color
     */
    public static Color prompt() {
        return (Color) IOManager.enumPrompt(Color.values(), "Цвет волос", " ");
    }

    /**
     * @param str string for parse
     * @return parsed color
     * @throws InvalidParameterException throws when no one color = str
     */
    public static Color parse(String str) throws InvalidParameterException {
        if (str.equals("null")) {
            return null;
        }
        try {
            return Color.valueOf(str);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Нет такого варианта цвета (" + str + ")");
        }
    }
}
