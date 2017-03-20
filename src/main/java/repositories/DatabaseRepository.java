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
import java.util.stream.Collectors;

/**
 * Created by Sergiu on 3/11/2017.
 */
public  class DatabaseRepository<E extends IEntity<Integer>> implements IDatabaseRepository<E,Integer> {
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
//            connection.commit();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return list;
    }

    @Override
    public E findById(Integer id,boolean startTransaction,boolean endTransaction) throws RepositoryException {
        String query = String.format("SELECT * FROM `%s` WHERE `%s` = ?",tableName,mapper.getIdTextField());
        ResultSet rs = null;
        PreparedStatement stmt;
        E el = null;
        try {
            if(startTransaction)
                connection.setAutoCommit(false);
            stmt = connection.prepareStatement(query);
            stmt.setString(1,id.toString());
            rs = stmt.executeQuery();
            while(rs.next()){
                el = mapper.toObject(rs);
            }
            if(endTransaction)
                connection.commit();
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
        finally {
            if(endTransaction){
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    codeThrowRepositoryException(e);
                }
            }
        }
        if(null == el)
            throw new RepositoryException("This id do not exist.");
        return el;
    }

    @Override
    public void update(E element,boolean startTransaction,boolean endTransaction) throws RepositoryException {
        String updateS = "";
        Map<String,String> map = mapper.toMap(element);
        List<String > values = new ArrayList<>();
        updateS += mapper.getIdTextField() + "=?";
        values.add(map.get(mapper.getIdTextField()));
        map.remove(mapper.getIdTextField());
        for(Map.Entry<String,String> entry : map.entrySet()){
            updateS += "," + entry.getKey() + "=?";
            values.add(entry.getValue());
        }
        String query = String.format("UPDATE `%s` SET %s WHERE `%s` = ?",tableName,updateS,mapper.getIdTextField());
        System.out.print(query);
        try {
            if(startTransaction)
                connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(query);
            for(Integer i=0;i<values.size();i++)
                statement.setString(i+1,values.get(i));
            statement.setString(values.size()+1,element.getId().toString());
            statement.executeUpdate();
            if(endTransaction)
                connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                codeThrowRepositoryException(e1);
            }
            codeThrowRepositoryException(e);
        }
        finally {
            if(endTransaction)
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    codeThrowRepositoryException(e);
                }
        }
    }

    @Override
    public Integer add(E element,boolean startTransaction,boolean endTransaction) throws RepositoryException {
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
        values = "?";
        if(valuesV.size() > 1)
            values = values + (new String(new char[valuesV.size() - 1]).replace("\0", " ,?"));
        System.out.println(params);
        System.out.println(values);

        String query = String.format("INSERT INTO `%s` (%s) VALUES (%s)",tableName,params,values);
        try {
            if(startTransaction)
                connection.setAutoCommit(false);
            PreparedStatement stmt = connection.prepareStatement(query);
            for(Integer i=0;i<valuesV.size();i++){
                stmt.setString(i+1,valuesV.get(i));
            }

            stmt.execute();
           // stmt.close();

            query = String.format("SELECT LAST_INSERT_ID() as id from `%s`",tableName);
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            returnedId = (rs.getInt(1));
            if(endTransaction){
                connection.commit();
            }

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
                if(endTransaction)
                    connection.setAutoCommit(true);
            } catch (SQLException e) {
                codeThrowRepositoryException(e);
            }
        }
        return returnedId;
    }

    @Override
    public E delete(Integer id,boolean startTransaction,boolean endTransaction) throws RepositoryException {
        String queryD =String.format("DELETE FROM `%s` WHERE `%s` = ?",tableName,mapper.getIdTextField());
        E el = null;

        try {
            if(startTransaction){
                connection.setAutoCommit(false);
            }
            el = findById(id,startTransaction,false);

            PreparedStatement stmt = connection.prepareStatement(queryD);
            stmt.setString(1,id.toString());
            stmt.executeUpdate();
            if(endTransaction)
                connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                codeThrowRepositoryException(e1);
            }
            codeThrowRepositoryException(e);
        }
        finally {
            if(endTransaction)
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    codeThrowRepositoryException(e);
                }
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
//            connection.commit();
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
//            connection.commit();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
    }

    protected void codeThrowRepositoryException(Exception exc) throws RepositoryException {
        throw new RepositoryException(exc);
    }

    public List<E> getItemsByProperty(Map<String,String> map) throws RepositoryException {
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
                E el = mapper.toObject(rs);
                list.add(el);
            }
//            connection.commit();
        } catch (SQLException e) {
            codeThrowRepositoryException(e);
        }
        return list;
    }
}
