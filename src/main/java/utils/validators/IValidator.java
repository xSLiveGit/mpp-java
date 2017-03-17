package utils.validators;

import utils.exceptions.RepositoryException;

/**
 * Created by Sergiu on 3/11/2017.
 */
public interface IValidator<E> {
    void validate(E el) throws RepositoryException;
}
