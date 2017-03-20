package repositories;

import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sergiu on 3/19/2017.
 */
public interface IDatabaseRepository<E,ID> {
    List<E> getItemsByProperty(Map<String,String> map) throws RepositoryException;
    List<E> getAll() throws RepositoryException;
    E findById(ID id,boolean startTransaction,boolean endTransaction) throws RepositoryException;
    void update(E element,boolean startTransaction,boolean endTransaction) throws RepositoryException;
    ID add(E element,boolean startTransaction,boolean endTransaction) throws RepositoryException, SQLException;
    E delete(ID id,boolean startTransaction,boolean endTransaction) throws RepositoryException;
    Integer getSize() throws RepositoryException, SQLException;
    void clear() throws RepositoryException;
}
