package entity;

/**
 * Created by Sergiu on 3/11/2017.
 */
public interface IEntity<ID> {
    ID getId();
    void setId(ID id);
}
