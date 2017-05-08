package repository;


import entity.IEntity;
import exceptions.EntityArgumentException;
import exceptions.RepositoryException;
import utils.DatabaseConnectionManager;
import utils.validator.IValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Sergiu on 3/11/2017.
 */
public abstract class AbstractDatabaseRepository<E extends IEntity<ID>,ID> implements IDatabaseRepository<E,ID> {

    protected String tableName;
    protected Connection connection;
    protected IValidator<E> validator;
    DatabaseConnectionManager databaseConnectionManager;
    public AbstractDatabaseRepository(DatabaseConnectionManager dbConnManager, String tableName, IValidator<E> validator) throws RepositoryException {
        try {
            this.connection = dbConnManager.getConnection();
            this.tableName = tableName;
            this.validator = validator;
            this.databaseConnectionManager = dbConnManager;
        } catch (SQLException | ClassNotFoundException e) {
            codeThrowRepositoryException(e);
        }
    }

    @Override
    public List<E> getAll() throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM `%s`",tableName);
        ArrayList<E> list = new ArrayList<E>();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                E el = this.toObject(rs);
                list.add(el);
            }
//            connection.commit();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return list;
    }

    @Override
    public E findById(ID id) throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT * FROM `%s` WHERE `%s` = ?",tableName,this.getIdTextField());
        ResultSet rs = null;
        PreparedStatement stmt;
        E el = null;
        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1,id.toString());
            rs = stmt.executeQuery();
            while(rs.next()) {
                el = this.toObject(rs);
            }
            rs.close();
        }
        catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                codeThrowRepositoryException(e1);
            }
            codeThrowRepositoryException(e);
        }
        if(null == el)
            throw new RepositoryException("This id do not exist.");
        return el;
    }

    @Override
    public void update(E element) throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        validator.validate(element);
        String updateS = "";
        Map<String,String> map = this.toMap(element);
        List<String > values = new ArrayList<>();
        updateS += this.getIdTextField() + "=?";
        values.add(map.get(this.getIdTextField()));
        map.remove(this.getIdTextField());
        for(Map.Entry<String,String> entry : map.entrySet()){
            updateS += "," + entry.getKey() + "=?";
            values.add(entry.getValue());
        }
        String query = String.format("UPDATE `%s` SET %s WHERE `%s` = ?",tableName,updateS,this.getIdTextField());
        System.out.print(query);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for(Integer i=0;i<values.size();i++)
                statement.setString(i+1,values.get(i));
            statement.setString(values.size()+1,element.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                codeThrowRepositoryException(e1);
            }
            codeThrowRepositoryException(e);
        }
    }

    @Override
    public void add(E element) throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        validator.validate(element);

        ArrayList<String> paramsV = new ArrayList<>();
        ArrayList<String> valuesV = new ArrayList<>();
        String params = "";
        String values = "";
        Map<String,String> map;
        Integer returnedId = null;

        map = this.toMap(element);
        for(Map.Entry<String,String> pair : map.entrySet()){
            valuesV.add(pair.getValue());
            paramsV.add("`" + pair.getKey() + "`");
        }

        params = String.join(",",paramsV);
        values = "?";
        if(valuesV.size() > 1)
            values = values + (new String(new char[valuesV.size() - 1]).replace("\0", " ,?"));

        String query = String.format("INSERT INTO `%s` (%s) VALUES (%s)",tableName,params,values);
        try {

            PreparedStatement stmt = connection.prepareStatement(query);
            for(Integer i=0;i<valuesV.size();i++){
                stmt.setString(i+1,valuesV.get(i));
            }

            stmt.execute();
            stmt.close();

        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }

    }

    @Override
    public E delete(ID id) throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String queryD =String.format("DELETE FROM `%s` WHERE `%s` = ?",tableName,this.getIdTextField());
        E el = null;

        try {
            el = findById(id);

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
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String query = String.format("SELECT COUNT(*) as sz FROM `%s`",tableName);
        Integer a = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            a = rs.getInt("sz");
//            connection.commit();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return a;
    }

    @Override
    public void clear() throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    protected void codeThrowRepositoryException(String exc) throws RepositoryException {
        throw new RepositoryException(exc);
    }

    public List<E> getItemsByProperty(Map<String,String> map) throws RepositoryException {
        try {
            this.connection = databaseConnectionManager.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> paramsV = new ArrayList<>();
        ArrayList<String> valuesV = new ArrayList<>();
        ArrayList<E> list = new ArrayList<E>();

        for(Map.Entry<String,String> entry : map.entrySet()){
            paramsV.add(entry.getKey());
            valuesV.add(entry.getValue());
        }//I done this to be sure that parameters with linked values are in the same order
        String condition = String.join(" AND ",paramsV.stream().map(x -> x = x + "=?").collect(Collectors.toList()));
        String query = String.format("SELECT * FROM `%s` WHERE %s",tableName,condition);
        System.out.println(query);
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            for(Integer i=0;i<valuesV.size();i++)
                stmt.setString(i+1,valuesV.get(i));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                E el = this.toObject(rs);
                list.add(el);
            }
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return list;
    }

    protected abstract String getIdTextField();
    protected abstract E toObject(ResultSet row) throws SQLException, EntityArgumentException;
    protected abstract Map<String,String> toMap(E object);
}
