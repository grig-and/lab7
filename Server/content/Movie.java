package content;

import exceptions.InvalidParameterException;
import org.apache.commons.lang3.ArrayUtils;
import util.IOManager;
import util.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Objects;

/**
 * Movie class
 */
public class Movie implements Serializable {
    private static final long serialVersionUID = 8243L;
    private static long lastId = 1;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private String owner = "";

    private MovieGenre genre; //Поле может быть null
    private MpaaRating mpaaRating; //Поле может быть null
    private Person operator; //Поле может быть null


    /**
     * @param name        Name
     * @param coordinates Coordinates
     * @param oscarsCount Number of Oscars
     * @param genre       Genre
     * @param mpaaRating  Mpaa rating
     * @param operator    Operator
     */
    public Movie(String name, Coordinates coordinates, Integer oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator) {
        this.id = ++lastId;
        this.creationDate = LocalDate.now();
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    /**
     * @param id           id
     * @param creationDate date of create
     * @param name         Name
     * @param coordinates  Coordinates
     * @param oscarsCount  Number of Oscars
     * @param genre        Genre
     * @param mpaaRating   Mpaa rating
     * @param operator     Operator
     */
    public Movie(long id, LocalDate creationDate, String name, Coordinates coordinates, Integer oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.coordinates = coordinates;
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    /**
     * Prompt a movie from user
     *
     * @return requested movie
     */
    public static Movie prompt() {
        String name = promptName();
        Coordinates coordinates = Coordinates.prompt();
        int oscarsCount = promptOC();
        MovieGenre genre = MovieGenre.prompt();
        MpaaRating mpaaRating = MpaaRating.prompt();
        Person operator = Person.prompt();


        return new Movie(
                name,
                coordinates,
                oscarsCount,
                genre,
                mpaaRating,
                operator
        );
    }

    /**
     * Prompt oscars count from user
     *
     * @return requested oscars count
     */
    private static int promptOC() {
        String str = IOManager.prompt("Кол-во оскаров (целое число, > 0): ");
        try {
            return parseOC(str);
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
            return promptOC();
        }
    }

    /**
     * @param str string for parse
     * @return oscars count
     * @throws InvalidParameterException
     */
    private static int parseOC(String str) throws InvalidParameterException {
        int oscarsCount;
        try {
            oscarsCount = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Кол-во оскаров должно быть целым числом");
        }
        if (oscarsCount <= 0) {
            throw new InvalidParameterException("Кол-во оскаров должно быть больше 0");
        }
        return oscarsCount;
    }

    /**
     * Prompt name from user
     *
     * @return requested name
     */
    private static String promptName() {
        String str = IOManager.prompt("Название: ");
        try {
            return parseName(str);
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
        }
        return promptName();
    }

    /**
     * @param str string for parse
     * @return name
     * @throws InvalidParameterException
     */
    private static String parseName(String str) throws InvalidParameterException {
        if (str.isEmpty()) {
            throw new InvalidParameterException("Название не может быть пустым");
        }
        return str;
    }


    /**
     * @return genre
     */
    public MovieGenre getGenre() {
        return genre;
    }

    /**
     * @return oscars count
     */
    public Integer getOscarsCount() {
        return oscarsCount;
    }

    /**
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Used ids
     */
    private static HashSet<Long> ids = new HashSet<>();

    /**
     * @param str string for parse
     * @return id
     * @throws InvalidParameterException
     */
    private static long parseId(String str) throws InvalidParameterException {
        long id = 0;
        try {
            id = Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("id должен быть целым числом");
        }

        if (id <= 0) {
            throw new InvalidParameterException("id должен быть > 0");
        }
        if (ids.contains(id)) {
            throw new InvalidParameterException("id не уникален");
        }
        ids.add(id);
        return id;
    }

    /**
     * Movie from array
     *
     * @param data array  from CSV
     * @return movie
     * @throws InvalidParameterException
     */
    public static Movie createMovie(String[] data) throws InvalidParameterException {
        long id = parseId(data[1]);
        if (id > lastId) lastId = id;
        LocalDate date = parseDate(data[2]);
        String name = parseName(data[3]);
        float x = Coordinates.parseX(data[4]);
        Integer y = Coordinates.parseY(data[5]);
        Coordinates coordinates = new Coordinates(x, y);
        int oscarsCount = parseOC(data[6]);
        MovieGenre genre = MovieGenre.parse(data[7]);
        MpaaRating mpaaRating = MpaaRating.parse(data[8]);
        String nameP = Person.parseName(data[9]);
        long height = Person.parseHeight(data[10]);
        String passportID = Person.parsePID(data[11]);
        Color hairColor = Color.parse(data[12]);
        Country nationality = Country.parse(data[13]);

        Person operator = new Person(nameP, height, passportID, hairColor, nationality);
        Movie movie = new Movie(
                id,
                date,
                name,
                coordinates,
                oscarsCount,
                genre,
                mpaaRating,
                operator
        );
        return movie;
    }

    /**
     * @param str string for parse
     * @return date
     * @throws InvalidParameterException
     */
    private static LocalDate parseDate(String str) throws InvalidParameterException {
        try {
            return LocalDate.parse(str);
        } catch (DateTimeParseException e) {
            throw new InvalidParameterException("Неверная дата");
        }
    }

    /**
     * @return movie as String array
     */
    public String[] getCSVMovie(String str) {
        String[] arr = ArrayUtils.addAll(new String[]{str,
                        Long.toString(id),
                        creationDate.toString(),
                        name,
                        Float.toString(coordinates.getX()),
                        coordinates.getY().toString(),
                        oscarsCount.toString(),
                        genre + "",
                        mpaaRating + ""},
                operator.getCSVPerson());
        return arr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Фильм\n" +
                "id: " + id +
                "\nСоздатель: " + owner +
                "\nНазвание: " + name +
                coordinates +
                "\nДата создания: " + creationDate +
                "\nКол-во оскаров: " + oscarsCount +
                "\nЖанр: " + (genre == null ? "не установлено" : genre) +
                "\nMpaa рейтинг: " + (mpaaRating == null ? "не установлено" : mpaaRating) +
                "\nОператор: " + (operator == null ? "не установлено" : operator) + "\n";
    }

    public MpaaRating getRating() {
        return mpaaRating;
    }

    public Person getOperator() {
        return operator;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}