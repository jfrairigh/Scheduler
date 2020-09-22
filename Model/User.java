package Model;

public class User {
    String userName, password;
    int userId;

    public User(String userName, String password, int userId) {
        this.userName = userName;
        this.password = password;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserId() {
        return userId;
    } 

    @Override
    public String toString() {
        return userName;
    }
    
}
