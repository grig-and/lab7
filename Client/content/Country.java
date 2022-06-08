package content;

import exceptions.InvalidParameterException;
import util.IO;

/**
 * Country enum
 */
public enum Country {
    /**
     * UK enum value
     */
    UNITED_KINGDOM,
    /**
     * Germany enum value
     */
    GERMANY,
    /**
     * Spain enum value
     */
    SPAIN;

    /**
     * Prompt a country from user
     *
     * @return requested country
     */
    public static Country prompt() {
        return (Country) IO.enumPrompt(Country.values(), "Национальность", " ");
    }

    /**
     * @param str string for parse
     * @return parsed country
     * @throws InvalidParameterException throws when no one country = str
     */
    public static Country parse(String str) throws InvalidParameterException {
        if (str.equals("null")) {
            return null;
        }
        try {
            return Country.valueOf(str);
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Нет такого варианта национальности (" + str + ")");
        }

    }
}