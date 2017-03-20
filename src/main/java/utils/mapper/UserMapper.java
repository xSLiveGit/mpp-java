package utils.mapper;

import domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/20/2017.
 */
public class UserMapper implements IMapper<User> {
    @Override
    public String getIdTextField() {
        return "id";
    }

    @Override
    public User toObject(ResultSet row) throws SQLException {
        Integer id = row.getInt(1);
        String user = row.getString(2);
        String password = row.getString(3);
        return new User(id,user,password);
    }

    @Override
    public Map<String, String> toMap(User object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("username",object.getUsername());
        map.put("password",object.getPassword());
        return map;
    }
}
