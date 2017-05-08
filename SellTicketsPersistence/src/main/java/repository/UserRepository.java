package repository;



import entity.User;
import exceptions.RepositoryException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.DatabaseConnectionManager;
import utils.validator.IValidator;
import utils.validator.ValidatorUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class UserRepository implements IRepository<User,String> {

    ValidatorUser validator;

    public UserRepository(ValidatorUser validator) {
        this.validator = validator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAll() throws RepositoryException {
        Transaction tx = null;
        Session session = DatabaseConnection.newSession();
        tx = session.beginTransaction();
        List<User> all =  (List<User>)session.createCriteria("User").list();
        tx.commit();
        session.close();
        return all;
    }

    @Override
    public User findById(String s) throws RepositoryException {
        Transaction tx = null;
        System.out.println("Iniante");
        Session session = DatabaseConnection.newSession();
        System.out.println("dupa");

        tx = session.beginTransaction();
        User user = session.get(User.class,s);
        System.out.println(user.getUsername() + " " +user.getPassword());
        tx.commit();
        return user;
    }

    @Override
    public void update(User element) throws RepositoryException {

    }

    @Override
    public String add(User element) throws RepositoryException, SQLException {
        return null;
    }

    @Override
    public User delete(String s) throws RepositoryException {
        return null;
    }

    @Override
    public Integer getSize() throws RepositoryException, SQLException {
        return null;
    }

    @Override
    public void clear() throws RepositoryException {

    }
}
