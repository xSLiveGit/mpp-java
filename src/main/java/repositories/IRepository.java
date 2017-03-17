package repositories;

import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Sergiu on 3/11/2017.
 */
public interface IRepository<E,ID> {
    List<E> getAll() throws RepositoryException;
    E findById(ID id) throws RepositoryException;
    void update(E element) throws RepositoryException;
    ID add(E element) throws RepositoryException, SQLException;
    E delete(ID id) throws RepositoryException;
    Integer getSize() throws RepositoryException, SQLException;
    void clear() throws RepositoryException;
}
