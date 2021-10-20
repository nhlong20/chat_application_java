package server.entities;

/**
 * server
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 4:12 PM
 * @Description
 */
public class Account {
    private String username;
    private String password;
    private boolean isOnline;
    private static final String REGEX = " ";

    public Account() {
        this.isOnline = false;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.isOnline = false;
    }
    public Account(String line) {
        String[] tokens = line.split(REGEX);
        this.username = tokens[0];
        this.password = tokens[1];
        this.isOnline = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + REGEX + password;
    }
}
