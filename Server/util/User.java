package util;

public class User {
    private String name;
    private String hash;
    private String salt;

    public User(String name, String hash, String salt) {
        this.name = name;
        this.hash = hash;
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }
}
