package content;

import exceptions.InvalidParameterException;
import util.IOManager;

import java.io.Serializable;

/**
 * Person class
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 8243L;
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long height; //Значение поля должно быть больше 0
    private String passportID; //Длина строки не должна быть больше 37, Поле не может быть null
    private Color hairColor; //Поле может быть null
    private Country nationality; //Поле может быть null

    /**
     * Person constructor
     *
     * @param name        name
     * @param height      height
     * @param passportID  passport ID
     * @param hairColor   hair color
     * @param nationality nationality
     */
    public Person(String name, long height, String passportID, Color hairColor, Country nationality) {
        this.name = name;
        this.height = height;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    /**
     * @return Person from user input
     */
    public static Person prompt() {
        System.out.println("Оператор:");
        String nameP = promptName();
        long height = promptHeight();
        String passportID = promptPID();
        Color hairColor = Color.prompt();
        Country nationality = Country.prompt();

        return new Person(nameP, height, passportID, hairColor, nationality);
    }

    /**
     * @return Height from user input
     */
    private static long promptHeight() {
        try {
            return parseHeight(IOManager.prompt(" Рост: "));
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptHeight();
        }
    }

    /**
     * @param str string for parse
     * @return height
     * @throws InvalidParameterException
     */
    public static long parseHeight(String str) throws InvalidParameterException {
        long height;
        try {
            height = Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(" Рост должен быть числом");
        }
        if (height <= 0) {
            throw new InvalidParameterException(" Рост должен быть больше 0");
        }
        return height;
    }

    /**
     * @return name from user input
     */
    private static String promptName() {
        try {
            return parseName(IOManager.prompt(" Имя: "));
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptName();
        }
    }

    /**
     * @param str string for parse
     * @return name
     * @throws InvalidParameterException
     */
    public static String parseName(String str) throws InvalidParameterException {
        if (str.isEmpty()) {
            throw new InvalidParameterException(" Имя не может быть пустым");
        }
        return str;
    }

    /**
     * @return PassportID from user input
     */
    private static String promptPID() {
        try {
            return parsePID(IOManager.prompt(" PassportID (не более 37 символов): "));
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptPID();
        }
    }

    /**
     * @param str string for parse
     * @return Passport ID
     * @throws InvalidParameterException
     */
    public static String parsePID(String str) throws InvalidParameterException {
        if (str.isEmpty()) {
            throw new InvalidParameterException(" PassportID не может быть пустым");
        } else if (str.length() > 37) {
            throw new InvalidParameterException(" Максимальная длина PassportID = 37");
        }
        return str;
    }

    @Override
    public String toString() {
        return "\n Имя: " + name +
                "\n Рост: " + height +
                "\n PassportID: " + passportID +
                "\n Цвет волос: " + (hairColor == null ? "не установлено" : hairColor) +
                "\n Национальность: " + (nationality == null ? "не установлено" : nationality);
    }

    public String[] getCSVPerson() {
        return new String[]{name,
                Long.toString(height),
                passportID,
                hairColor + "",
                nationality + ""};
    }

    public String getName() {
        return name;
    }

    public long getHeight() {
        return height;
    }

    public String getPassportID() {
        return passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Country getNationality() {
        return nationality;
    }
}