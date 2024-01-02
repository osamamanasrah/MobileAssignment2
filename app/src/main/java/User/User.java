package User;

public class User {
    private String userName;
    private String email;
    private String password;
    private boolean remembered;

    public User() {
    }
    public User(String userName, String email, String password, boolean remembered) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.remembered = remembered;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemembered() {
        return remembered;
    }

    public void setRemembered(boolean remembered) {
        this.remembered = remembered;
    }
}
