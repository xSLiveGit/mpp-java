package entity;

import java.io.Serializable;

/**
 * Created by Sergiu on 1/19/2017.
 */
public class User implements IEntity<String> ,Serializable {
    private String username;
    private String password;
    private Integer id;

    @Override
    public String getId() {
        return this.getUsername();
    }

    @Override
    public void setId(String s) {
        setUsername(s);
    }


    public User(String username, String password) {
        this(-1,username,password);
    }

    public User(Integer id,String username,String password) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getPassword(){
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!username.equals(user.username)) return false;
        if (!password.equals(user.password)) return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
