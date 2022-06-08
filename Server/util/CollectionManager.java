package util;

import content.Movie;
import content.MovieGenre;
import exceptions.DBError;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Collection manager class
 */
public class CollectionManager {
    private TreeMap<String, Movie> movies;
    private final LocalDate date;
    ReadWriteLock lk = new ReentrantReadWriteLock();
    private static Log log = new Log();
    private DataBase db = DataBase.getInstance();

    /**
     * Constructor
     */
    public CollectionManager() {
        DataBase db = DataBase.getInstance();
        this.movies = db.getAll();
        this.date = LocalDate.now();
    }

    /**
     * Clear collection
     */
    public boolean clear(String login) {
        if (lk.writeLock().tryLock()) {
            try {
                if (db.clear(login)) {
                    movies = movies.entrySet().stream()
                            .filter((e) -> e.getValue().getOwner().equals(login))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, TreeMap::new));
                    return true;
                }
            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }
        return false;
    }

    @Override
    public String toString() {
        return movies
                .entrySet()
                .stream()
                .map((e) -> e.getKey() + ":\n" + e.getValue().toString() + "\n")
                .sorted(Comparator.comparingInt(String::length))
                .reduce((a, b) -> a + b)
                .orElse("Коллекция пуста\n")
                .trim();
    }

    /**
     * Filter movie greater then genre
     *
     * @param genre MovieGenre for filter
     * @return filtered movies
     */
    public String filterGreaterThanGenre(MovieGenre genre) {
        return movies
                .values()
                .stream()
                .filter((v) -> v.getGenre().compareTo(genre) > 0)
                .map((s) -> s + "\n")
                .sorted(Comparator.comparingInt(String::length))
                .reduce((a, b) -> a + b)
                .orElse("Нет таких элементов коллекции");
    }

    /**
     * @return info about collection
     */
    public String getInfo() {
        return "type: " + movies.getClass() + "\n" + "date: " + date + "\nsize: " + movies.size();
    }

    /**
     * @param key key
     * @return is collection contains key
     */
    public boolean contains(String key) {
        return movies.containsKey(key);
    }

    public boolean containsID(Long id) {
        return movies.values().stream().anyMatch((v) -> v.getId() == id);
    }

    /**
     * Insert movie by key
     *
     * @param key   key
     * @param movie movie
     */
    public boolean insert(String key, Movie movie, String login) {
        if (lk.writeLock().tryLock()) {
            try {
                if (contains(key)) return false;

                if (db.insert(key, movie, login)) {
                    movie.setOwner(login);
                    movies.put(key, movie);
                    return true;
                }

            } catch (DBError e) {
                e.printStackTrace();
            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }
        return false;
    }

    /**
     * @return movies in ascending by OscarsCount order
     */
    public String getAscending() {
        return movies.entrySet().stream()
                .sorted((e1, e2) -> {
                    Movie v1 = e1.getValue();
                    Movie v2 = e2.getValue();
                    return v1.getOscarsCount().compareTo(v2.getOscarsCount());
                })
                .map((s) -> s + "\n")
                .sorted(Comparator.comparingInt(String::length))
                .reduce((a, b) -> a + b)
                .orElse("Коллекция пуста\n");
    }

    /**
     * Remove all elements with OC > entered
     *
     * @param movie movie for comparation
     * @return n of removed elements
     */
    public int removeGreater(Movie movie, String login) {
        AtomicInteger i = new AtomicInteger();

        if (lk.writeLock().tryLock()) {
            try {
                if (db.removeGreater(movie.getOscarsCount(), login)) {
                    movies = movies.entrySet().stream()
                            .filter((e) -> {
                                if ((e.getValue().getOscarsCount() > movie.getOscarsCount()) && (e.getValue().getOwner().equals(login))) {
                                    i.getAndIncrement();
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, TreeMap::new));
                }

            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }


        return i.get();
    }

    /**
     * Remove all with key > entered key
     *
     * @param key key
     * @return n of removed elements
     */
    public int removeGreaterKey(String key, String login) {
        AtomicInteger i = new AtomicInteger();

        if (lk.writeLock().tryLock()) {
            try {

                if (db.removeGreaterKey(key, login)) {
                    movies = movies.entrySet().stream()
                            .filter((e) -> {
                                if ((e.getKey().length() > key.length()) && (e.getValue().getOwner().equals(login))) {
                                    i.getAndIncrement();
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, TreeMap::new));
                }

            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }
        return i.get();
    }

    /**
     * Remove by key
     *
     * @param key key
     * @return
     */
    public boolean removeKey(String key, String login) {
        if (lk.writeLock().tryLock()) {
            try {
                if (movies.get(key).getOwner().equals(login) && db.removeKey(key, login)) {
                    movies.remove(key);
                    return true;
                }
            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }
        return false;
    }

    /**
     * @return sum of oscars
     */
    public int getSumOscars() {
        int n = 0;
        for (Movie movie : movies.values()) {
            n += movie.getOscarsCount();
        }
        return n;
    }

    /**
     * Update existing element
     *
     * @param id    id
     * @param movie movie
     */
    public void update(Long id, Movie movie, String login) throws DBError {
        if (lk.writeLock().tryLock()) {
            try {
                if (!db.update(id, movie, login)) {
                    throw new DBError("Ошибка при обновлении в бд");
                }

                movies = movies.entrySet().stream()
                        .peek((e) -> {
                            if (e.getValue().getId() == id) {
                                e.setValue(movie);
                            }
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, TreeMap::new));
            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }
    }

    /**
     * Replace if OC greater
     *
     * @param key   key
     * @param movie movie
     */
    public boolean replaceIfGreater(String key, Movie movie, String login) {
        AtomicBoolean res = new AtomicBoolean(false);
        if (lk.writeLock().tryLock()) {
            try {
                try {
                    if (db.replaceIfGreater(key, movie, login)) {
                        movies = movies.entrySet().stream()
                                .peek((e) -> {
                                    if ((e.getKey().equals(key)) && (e.getValue().getOscarsCount() < movie.getOscarsCount()) && (e.getValue().getOwner().equals(login))) {
                                        e.setValue(movie);
                                        res.set(true);
                                    }
                                })
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (l, r) -> l, TreeMap::new));
                    }
                } catch (DBError e) {
                    log.error(e.getMessage());
                }
            } finally {
                lk.writeLock().unlock();
            }
        } else {
            log.error("Попытка параллельной записи");
        }

        return res.get();
    }

}
