package utils.database;

/**
 * Created by Sergiu on 3/11/2017.
 */
public interface IDatabaseManager<T extends ID,ID> {
    void add(T object);
    void delete(ID id);
}
