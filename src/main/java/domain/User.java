package domain;

/**
 * Created by Sergiu on 1/19/2017.
 */
public class User implements IEntity<Integer> {
    private String username;
    private String password;
    private Integer id;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
}
