package repositories;

import domain.IEntity;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.RepositoryException;
import utils.mapper.IMapper;
import utils.validators.IValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiu on 3/11/2017.
 */
public  class DatabaseRepository<E extends IEntity<Integer>> implements IRepository<E,Integer> {
    protected String tableName;
    protected Connection connection;
    protected IMapper<E> mapper;
    protected IValidator<E> validator;
    public DatabaseRepository(DatabaseConnectionManager dbConnManager,IMapper<E> mapper,String tableName,IValidator<E> validator) throws RepositoryException {
        try {
            this.mapper = mapper;
            this.connection = dbConnManager.getConnection();
            this.tableName = tableName;
            this.validator = validator;
        } catch (SQLException | ClassNotFoundException e) {
            codeThrowRepositoryException(e);
        }
    }

    @Override
    public List<E> getAll() throws RepositoryException {
        String query = String.format("SELECT * FROM `%s`",tableName);
        ArrayList<E> list = new ArrayList<E>();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                E el = mapper.toObject(rs);
                list.add(el);
            }
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return list;
    }

    @Override
    public E findById(Integer id) throws RepositoryException {
        String query = String.format("SELECT * FROM `%s` WHERE `%s` = ?",tableName,mapper.getIdTextField());
        ResultSet rs = null;
        PreparedStatement stmt;
        E el = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1,id.toString());
            rs = stmt.executeQuery();
            while(rs.next()){
                el = mapper.toObject(rs);
            }
            rs.close();
        }
        catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        if(null == el)
            throw new RepositoryException("This id do not exist.");
        return el;
    }

    @Override
    public void update(E element) throws RepositoryException {
        String updateS = "";
        Map<String,String> map = mapper.toMap(element);
        updateS += mapper.getIdTextField() + "=" + map.get(mapper.getIdTextField());
        map.remove(mapper.getIdTextField());
        for(Map.Entry<String,String> entry : map.entrySet()){
            updateS += "," + entry.getKey() + "=" + entry.getValue() + " ";
        }
        String query = String.format("UPDATE `%s` SET %s WHERE `%s` = ?",tableName,updateS,mapper.getIdTextField());
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,element.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
    }

    @Override
    public Integer add(E element) throws RepositoryException {
        ArrayList<String> paramsV = new ArrayList<>();
        ArrayList<String> valuesV = new ArrayList<>();
        String params = "";
        String values = "";
        Map<String,String> map;
        Integer returnedId = null;

        map = mapper.toMap(element);
        map.remove(mapper.getIdTextField());
        for(Map.Entry<String,String> pair : map.entrySet()){
            valuesV.add(pair.getValue());
            paramsV.add("`" + pair.getKey() + "`");
        }

        params = String.join(",",paramsV);
        values = String.join(",",valuesV);

        System.out.println(params);
        System.out.println(values);

        String query = String.format("INSERT INTO `%s` (%s) VALUES (%s)",tableName,params,values);
        try {
            connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(query);
//            stmt.setString(1,params);
//            stmt.setString(2,values);
            stmt.execute();
           // stmt.close();

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

    @Override
    public E delete(Integer id) throws RepositoryException {
        String queryD =String.format("DELETE FROM `%s` WHERE `%s` = ?",tableName,mapper.getIdTextField());
        E el = findById(id);
        try {
            PreparedStatement stmt = connection.prepareStatement(queryD);
            stmt.setString(1,id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return el;
    }

    @Override
    public Integer getSize() throws RepositoryException {
        String query = String.format("SELECT COUNT(*) as sz FROM `%s`",tableName);
        Integer a = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            a = rs.getInt("sz");
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return a;
    }

    @Override
    public void clear() throws RepositoryException {
        String query = String.format("DELETE FROM `%s`", tableName);
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.execute();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
    }

    protected void codeThrowRepositoryException(Exception exc) throws RepositoryException {
        throw new RepositoryException(exc);
    }

}
