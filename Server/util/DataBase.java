package util;

import content.*;
import exceptions.DBError;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DataBase {
    private static DataBase instance = null;
    Connection conn;

    private DataBase() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://pg:5432/studs", "", "");
            PreparedStatement st = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS \"users\" (" +
                            "login VARCHAR(20) PRIMARY KEY," +
                            "hash VARCHAR(100) NOT NULL," +
                            "salt VARCHAR(10) NOT NULL" +
                            ");" +

                            "CREATE TABLE IF NOT EXISTS movies (" +
                            "key VARCHAR(20) NOT NULL UNIQUE," +
                            "id SERIAL PRIMARY KEY," +
                            "owner VARCHAR(20) references \"users\"(login) ON DELETE SET NULL," +
                            "name VARCHAR(20) NOT NULL," +
                            "creation_date TIMESTAMP NOT NULL," +

                            "coord_x FLOAT," +
                            "coord_y INTEGER NOT NULL," +

                            "oscars_count INTEGER NOT NULL," +
                            "genre VARCHAR(10)," +
                            "mpaa_rating VARCHAR(5)," +
                            "operator_name VARCHAR(20) NOT NULL," +

                            "operator_height BIGINT NOT NULL," +
                            "operator_passport_id VARCHAR(37) NOT NULL," +
                            "operator_hair_color VARCHAR(6)," +
                            "operator_nationality VARCHAR(14)" +
                            ");"
            );
            st.executeUpdate();

        } catch (PSQLException e) {
            Log.getLog().error("Ошибка подключения к базе");
            System.exit(1);
        } catch (SQLException e) {
        }
    }


    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public TreeMap<String, Movie> getAll() {
        TreeMap<String, Movie> movies = new TreeMap<>();

        try {
            ResultSet res = conn.prepareStatement("SELECT * FROM movies").executeQuery();
            while (res.next()) {
                Movie movie = new Movie(
                        res.getInt("id"),
                        res.getDate("creation_date").toLocalDate(),
                        res.getString("name"),
                        new Coordinates(res.getFloat("coord_x"), res.getInt("coord_y")),
                        res.getInt("oscars_count"),
                        res.getString("genre") == null ? null : MovieGenre.valueOf(res.getString("genre")),
                        res.getString("mpaa_rating") == null ? null : MpaaRating.valueOf(res.getString("mpaa_rating")),
                        new Person(
                                res.getString("operator_name"),
                                res.getLong("operator_height"),
                                res.getString("operator_passport_id"),
                                res.getString("operator_hair_color") == null ? null : Color.valueOf(res.getString("operator_hair_color")),
                                res.getString("operator_nationality") == null ? null : Country.valueOf(res.getString("operator_nationality"))
                        )
                );
                movie.setOwner(res.getString("owner"));
                movies.put(res.getString("key"), movie);
            }
        } catch (SQLException | NullPointerException e) {

        }
        return movies;
    }

    public int add(String key, Movie movie, String login) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO movies(key, owner, name, creation_date, " +
                            "coord_x, coord_y, oscars_count, " +
                            "genre, mpaa_rating, " +
                            "operator_name, operator_height, operator_passport_id, operator_hair_color, operator_nationality)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                            "RETURNING id");
            ps.setString(1, key);
            ps.setString(2, login);
            ps.setString(3, movie.getName());
            ps.setDate(4, Date.valueOf(movie.getCreationDate()));
            ps.setDouble(5, movie.getCoordinates().getX());
            ps.setInt(6, movie.getCoordinates().getY());
            ps.setLong(7, movie.getOscarsCount());
            setNullableEnum(ps, 8, movie.getGenre());
            setNullableEnum(ps, 9, movie.getRating());
            ps.setString(10, movie.getOperator().getName());
            ps.setLong(11, movie.getOperator().getHeight());
            ps.setString(12, movie.getOperator().getPassportID());
            setNullableEnum(ps, 13, movie.getOperator().getHairColor());
            setNullableEnum(ps, 14, movie.getOperator().getNationality());
            ResultSet res = ps.executeQuery();
            res.next();
            return res.getInt("id");
        } catch (Exception e) {
            return -1;
        }

    }

    private void setNullableEnum(PreparedStatement ps, int place, Enum en) throws SQLException {
        if (en != null) {
            ps.setString(place, en.name());
        } else {
            ps.setNull(place, java.sql.Types.NULL);
        }
    }

    public User getUser(String login) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM \"users\" WHERE login = ?");
            st.setString(1, login);
            ResultSet res = st.executeQuery();
            if (res.next()) {
                return new User(login, res.getString("hash"), res.getString("salt"));
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public boolean isUserExist(String login) {
        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM \"users\" WHERE login = ?");
            st.setString(1, login);
            ResultSet res = st.executeQuery();
            return res.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isMovieExistById(long id, String login) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM movies WHERE id = ? AND owner = ?");
            ps.setLong(1, id);
            ps.setString(2, login);
            ResultSet res = ps.executeQuery();
            return res.next();
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isMovieExistByKey(String key) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM movies WHERE 'key' = ?");
            ps.setString(1, key);
            ResultSet res = ps.executeQuery();
            return res.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean insert(String key, Movie movie, String login) throws DBError {
        if (isMovieExistByKey(key)) {
            throw new DBError("Уже есть элемент с таким ключом");
        }
        return add(key, movie, login) != -1;
    }

    public boolean update(Long id, Movie movie, String login) throws DBError {
        if (!isMovieExistById(id, login)) {
            throw new DBError("Нет доступного элемента с таким id");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE movies SET name = ?, creation_date = ?, " +
                            "coord_x = ?, coord_y = ?, oscars_count = ?, " +
                            "genre = ?, mpaa_rating = ?, " +
                            "operator_name = ?, operator_height = ?, operator_passport_id = ?, operator_hair_color = ?, operator_nationality = ? " +
                            "WHERE id = ? AND owner = ?");

            ps.setString(1, movie.getName());
            ps.setDate(2, Date.valueOf(movie.getCreationDate()));
            ps.setDouble(3, movie.getCoordinates().getX());
            ps.setInt(4, movie.getCoordinates().getY());
            ps.setLong(5, movie.getOscarsCount());
            setNullableEnum(ps, 6, movie.getGenre());
            setNullableEnum(ps, 7, movie.getRating());
            ps.setString(8, movie.getOperator().getName());
            ps.setLong(9, movie.getOperator().getHeight());
            ps.setString(10, movie.getOperator().getPassportID());
            setNullableEnum(ps, 11, movie.getOperator().getHairColor());
            setNullableEnum(ps, 12, movie.getOperator().getNationality());
            ps.setLong(13, id);
            ps.setString(14, login);
            int res = ps.executeUpdate();

            return res > 0;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean addUser(String login, String hash, String salt) {
        try {
            if (isUserExist(login)) {
                return false;
            }
            PreparedStatement st = conn.prepareStatement("INSERT INTO \"users\" (login, hash, salt) VALUES (?, ?, ?)");
            st.setString(1, login);
            st.setString(2, hash);
            st.setString(3, salt);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean clear(String login) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE owner = ?");
            ps.setString(1, login);
            int res = ps.executeUpdate();
            return res > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeGreater(Integer oscarsCount, String login) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE oscars_count > ? AND owner = ?");
            ps.setInt(1, oscarsCount);
            ps.setString(2, login);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeGreaterKey(String key, String login) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE LENGTH(key) > ? AND owner = ?");
            ps.setInt(1, key.length());
            ps.setString(2, login);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {

            return false;
        }
    }

    public boolean removeKey(String key, String login) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM movies WHERE 'key' = ? AND owner = ?");
            ps.setString(1, key);
            ps.setString(2, login);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {

            return false;
        }
    }


    public boolean replaceIfGreater(String key, Movie movie, String login) throws DBError {
        if (!isMovieExistByKey(key)) {
            throw new DBError("Нет доступного элемента с таким id");
        }

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE movies SET name = ?, creation_date = ?, " +
                            "coord_x = ?, coord_y = ?, oscars_count = ?, " +
                            "genre = ?, mpaa_rating = ?, " +
                            "operator_name = ?, operator_height = ?, operator_passport_id = ?, operator_hair_color = ?, operator_nationality = ? " +
                            "WHERE 'key' = ? AND oscars_count > ? AND owner = ?");

            ps.setString(1, movie.getName());
            ps.setDate(2, Date.valueOf(movie.getCreationDate()));
            ps.setDouble(3, movie.getCoordinates().getX());
            ps.setInt(4, movie.getCoordinates().getY());
            ps.setLong(5, movie.getOscarsCount());
            setNullableEnum(ps, 6, movie.getGenre());
            setNullableEnum(ps, 7, movie.getRating());
            ps.setString(8, movie.getOperator().getName());
            ps.setLong(9, movie.getOperator().getHeight());
            ps.setString(10, movie.getOperator().getPassportID());
            setNullableEnum(ps, 11, movie.getOperator().getHairColor());
            setNullableEnum(ps, 12, movie.getOperator().getNationality());
            ps.setString(13, key);
            ps.setInt(14, movie.getOscarsCount());
            ps.setString(15, login);
            int res = ps.executeUpdate();

            return res > 0;
        } catch (Exception e) {
            return false;
        }

    }
}
