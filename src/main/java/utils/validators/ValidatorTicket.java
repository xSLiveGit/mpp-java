package utils.validators;

import domain.Ticket;
import utils.exceptions.RepositoryException;

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

    private void validatePrice(Double price){
        if(price < 0){
            errList.add("The price of tickets must be greater than 0");
        }
    }

    @Override
    public void validate(Ticket el) throws RepositoryException {
        errList.clear();
        validatePrice(el.getPrice());
        if(errList.size() != 0){
            throw new RepositoryException(errList.toString());
        }
    }
}
