package utils.validators;

import domain.Sale;
import utils.exceptions.RepositoryException;

import java.util.ArrayList;

/**
 * Created by Sergiu on 3/18/2017.
 */
public class ValidatorSale implements IValidator<Sale> {
    ArrayList<String> errList;
    private void validateQuantity(Integer quantity){
        if(quantity <=0)
            errList.add("Quantity must be positive number.");
    }

    private void validatePerson(String person){
        if(person.equals(""))
            errList.add("Person must be non-empty string.");
    }
    @Override
    public void validate(Sale el) throws RepositoryException {
        errList.clear();
        validatePerson(el.getPerson());
        validateQuantity(el.getQuantity());
        if(errList.size() > 0){
            throw new RepositoryException(String.join("\n"),errList);
        }
    }
}
