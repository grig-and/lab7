package util;

import exceptions.UserNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Auth {
    private static DataBase db = DataBase.getInstance();
    private static final String pepper = "q1*]gW9@,7";

    public static boolean checkRequest(Request req) throws UserNotFoundException {
        return login(req.getLogin(), req.getPassword());
    }

    public static boolean login(String username, String password) throws UserNotFoundException {
        User user = db.getUser(username);
        if (user == null) {
            throw new UserNotFoundException("Нет такого пользователя");
        }
        String hash = hash(password, user.getSalt());
        return hash.equals(user.getHash());
    }

    //register user
    public static boolean register(String username, String password) {
        if (db.getUser(username) != null) {
            return false;
        }
        String salt = generateSalt();
        return db.addUser(username, hash(password, salt), salt);
    }

    private static String generateSalt() {
        String salt = "";
        for (int i = 0; i < 10; i++) {
            salt += (char) (Math.random() * 26 + 97);
        }
        return salt;
    }

    public static String hash(String password, String salt) {
        String hash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] digest = md.digest((pepper + password + salt).getBytes());

            String hex = "";
            for (int i = 0; i < digest.length; i++) {
                hash += Integer.toHexString(digest[i] & 0xff);
            }
            return hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

}
