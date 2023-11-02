package next.model;

import java.util.Objects;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId) {
        this.userId = userId;
    }
    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name, email);
    }

    public boolean isSameUser(User user) {
        return this.userId.equals(user.userId);
    }

    public boolean matchPassword(String password) {
        if(password == null) return false;

        return this.password.equals(password);
    }

    public void update(User updateUser) {
        this.password = updateUser.password;
        this.name = updateUser.name;
        this.email = updateUser.email;
    }

    public boolean isSameUser(String writer) {
        return this.userId.equals(writer);
    }
}
