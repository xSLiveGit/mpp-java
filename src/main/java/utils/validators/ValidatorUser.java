package utils.validators;

import domain.User;
import utils.exceptions.RepositoryException;

import java.util.ArrayList;

/**
 * Created by Sergiu on 3/20/2017.
 */
public class ValidatorUser implements IValidator<User> {
    ArrayList<String> errList;

    public ValidatorUser() {
        errList = new ArrayList<>();
    }

    private void validateEntity(String entityValue, String entity)
    {
        if (entityValue.length() < 6 || entityValue.length() > 20)
        {
            errList.add(String.format("The length of %s must be part of [6,20]",entity));
        }
    }

    @Override
    public void validate(User el) throws RepositoryException {
        errList.clear();
        validateEntity(el.getUsername(),"username");
        validateEntity(el.getPassword(),"password");
        if(!errList.isEmpty())
            throw new RepositoryException(String.join("\n",errList));
    }
}
