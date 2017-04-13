package entity;

import java.io.Serializable;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class Sale implements Serializable {
    Integer idMatch = null;
    Integer quantity = null;
    String person = null;
    public Sale(Integer idMatch, Integer quantity,String person) {
        this.idMatch = idMatch;
        this.quantity = quantity;
        this.person = person;
    }

    public Integer getIdMatch() {
        return idMatch;
    }

    public String getPerson() {
        return person;
    }


    public Sale setPerson(String person) {

        this.person = person;
        return this;
    }

    public Sale setIdMatch(Integer idMatch) {
        this.idMatch = idMatch;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Sale setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
