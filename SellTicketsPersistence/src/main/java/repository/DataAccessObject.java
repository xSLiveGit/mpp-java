package repository;

import com.sun.org.apache.regexp.internal.RE;
import entity.IEntity;
import entity.User;
import exceptions.RepositoryException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 5/8/2017.
 */
public class DataAccessObject<T extends IEntity<ID>,ID extends Serializable> {

    public T findById(Class<T> className,ID key) throws RepositoryException {
        Transaction tx = null;
        Session session = DatabaseConnection.newSession();

        tx = session.beginTransaction();
        T item = session.get(className,key);
        tx.commit();
        return item;
    }

    public ID add(Class<T> className,T item) throws RepositoryException{
        ID key;
        try {
            if(null!=item.getId()&& this.findById(className,item.getId()) != null){
                throw new RepositoryException("This item already exists.");
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        Transaction tx = null;
        Session session = DatabaseConnection.newSession();
        tx = session.beginTransaction();
        session.save(item);
        tx.commit();
        session.close();
        key = item.getId();
        return key;
    }

    public void update(Class<T> className,T item) throws RepositoryException{
        T item2 = this.findById(className,item.getId());
        if(null == item2){
            throw new RepositoryException("This item do not exists");
        }
        Transaction transaction = null;
        Session session = DatabaseConnection.newSession();
        transaction = session.beginTransaction();
        session.update(item);
        transaction.commit();
    }

    public T delete(Class<T> className,ID key) throws RepositoryException{
        T item = this.findById(className,key);
        if(null == item){
            throw new RepositoryException("There do not exists any item with this key");
        }
        Transaction transaction = null;
        Session session = DatabaseConnection.newSession();
        transaction = session.beginTransaction();
        session.delete(item);
        transaction.commit();
        session.close();
        return item;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll(Class<T> className,String tableName) throws RepositoryException {
//        Transaction transaction = null;
//
//        List<T> list = new ArrayList<T>();
//        Session session = DatabaseConnection.newSession();
//        transaction = session.beginTransaction();
//        for (Object oneObject : session.createQuery(String.format("FROM %s",tableName)).getResultList()) {
//            list.add((T)oneObject);
//        }
//        session.close();
//        transaction.commit();
//        return list;
        Transaction tx = null;
        Session session = DatabaseConnection.newSession();
        tx = session.beginTransaction();
        List<T> all =  (List<T>)session.createCriteria(className).list();
        tx.commit();
        session.close();
        return all;
    }

    public Integer getSize(String tableName) throws RepositoryException {
        Transaction transaction = null;
        Session session = DatabaseConnection.newSession();
        transaction = session.beginTransaction();
        Integer count = (Integer)session.createQuery(String.format("select count(1) from  %s",tableName)).getSingleResult();
        return count;
    }

}
