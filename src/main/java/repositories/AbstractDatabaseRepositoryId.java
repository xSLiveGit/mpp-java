package repositories;

import domain.IEntity;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.RepositoryException;
import utils.validators.IValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Sergiu on 3/25/2017.
 */
public abstract class AbstractDatabaseRepositoryId<E extends IEntity<Integer>> extends AbstractDatabaseRepository<E,Integer> {
    public AbstractDatabaseRepositoryId(DatabaseConnectionManager dbConnManager, String tableName, IValidator<E> validator) throws RepositoryException {
        super(dbConnManager, tableName, validator);
    }

    public Integer addId(E element) throws RepositoryException {
        validator.validate(element);
        ArrayList<String> paramsV = new ArrayList<>();
        ArrayList<String> valuesV = new ArrayList<>();
        String params = "";
        String values = "";
        Map<String,String> map;
        Integer returnedId = null;

        map = this.toMap(element);
        map.remove(this.getIdTextField());
        for(Map.Entry<String,String> pair : map.entrySet()){
            valuesV.add(pair.getValue());
            paramsV.add("`" + pair.getKey() + "`");
        }

        params = String.join(",",paramsV);
        values = "?";
        if(valuesV.size() > 1)
            values = values + (new String(new char[valuesV.size() - 1]).replace("\0", " ,?"));
        System.out.println(params);
        System.out.println(values);

        String query = String.format("INSERT INTO `%s` (%s) VALUES (%s)",tableName,params,values);
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(query);
            for(Integer i=0;i<valuesV.size();i++){
                stmt.setString(i+1,valuesV.get(i));
            }

            stmt.execute();

            query = String.format("SELECT LAST_INSERT_ID() as id from `%s`",tableName);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            returnedId = (rs.getInt(1));
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                codeThrowRepositoryException(new RepositoryException(e.getMessage() + e1.getMessage()));
            }
            codeThrowRepositoryException(e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                codeThrowRepositoryException(e);
            }
        }
        return returnedId;
    }
}
