package content;

import exceptions.InvalidParameterException;
import util.IO;

import java.io.Serializable;

/**
 * Coordinates of movie class
 */
public class Coordinates implements Serializable {
    private static final long serialVersionUID = 8243L;
    private float x;
    private Integer y; //Поле не может быть null

    /**
     * Coordinates of movie constructor
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Coordinates(float x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * @return Y coordinate
     */
    public Integer getY() {
        return y;
    }

    /**
     * @return coordinates instance requested from user
     */
    public static Coordinates prompt() {
        float x = promptX();
        Integer y = promptY();
        return new Coordinates(x, y);
    }

    /**
     * @return X coordinate requested from user
     */
    private static float promptX() {
        try {
            return parseX(IO.prompt("Координаты: \n Введите число\n x: "));
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptX();
        }
    }

    /**
     * @param str string for parse
     * @return X coordinate
     * @throws InvalidParameterException
     */
    public static float parseX(String str) throws InvalidParameterException {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(" Не число");
        }
    }

    /**
     * @return Y coordinate requested from user
     */
    private static Integer promptY() {
        try {
            return parseY(IO.prompt(" Введите число\n y (целое число): "));
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptY();
        }
    }

    /**
     * @param str string for parse
     * @return Y coordinate
     * @throws InvalidParameterException
     */
    public static Integer parseY(String str) throws InvalidParameterException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(" Неверно введено число");
        }
    }

    @Override
    public String toString() {
        return "\nКоординаты: " +
                "\n x: " + x +
                "\n y: " + y;
    }
}
