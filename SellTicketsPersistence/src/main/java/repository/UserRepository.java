package repository;



import entity.User;
import exceptions.RepositoryException;
import utils.DatabaseConnectionManager;
import utils.validator.IValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class UserRepository extends AbstractDatabaseRepository<User,String> {

    public UserRepository(DatabaseConnectionManager dbConnManager, String tableName, IValidator<User> validator) throws RepositoryException {
        super(dbConnManager, tableName, validator);
    }

    @Override
    protected String getIdTextField() {
        return "username";
    }

    @Override
    protected User toObject(ResultSet row) throws SQLException {
        String user = row.getString(1);
        String password = row.getString(2);
        return new User(user,password);
    }

    @Override
    protected Map<String, String> toMap(User object) {
        Map<String,String> map = new HashMap<>();
        map.put("username",object.getUsername());
        map.put("password",object.getPassword());
        return map;
    }
}
