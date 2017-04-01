package utils.validator;



import entity.Ticket;
import exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class ValidatorTicket implements IValidator<Ticket> {
    private List<String> errList;

    public ValidatorTicket() {
        this.errList = new ArrayList<>();
    }

    private void validateQuantity(Integer quantity)
    {
        if (quantity <= 0)
        {
            errList.add("Quantity must be nonnegative number.");
        }
    }

    private void validatePerson(String person)
    {
        if(person.equals(""))
            errList.add("The person can't be empty.");
        if(person.length() > 30)
            errList.add("The length of person must be <= 30.");
    }


    @Override
    public void validate(Ticket el) throws RepositoryException {
        errList.clear();
        validateQuantity(el.getQuantity());
        validatePerson(el.getPerson());
        if(errList.size() != 0){
            throw new RepositoryException(errList.toString());
        }
    }
}
