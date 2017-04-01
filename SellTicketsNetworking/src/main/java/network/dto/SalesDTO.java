package network.dto;

import java.io.Serializable;

/**
 * Created by Sergiu on 3/30/2017.
 */
public class SalesDTO implements Serializable {
    private String idMatch = null;
    private String quantity = null;
    private String person = null;

    public SalesDTO(String idMatch, String quantity,String person) {
        this.idMatch = idMatch;
        this.quantity = quantity;
        this.person = person;
    }

    public String getPerson() {
        return person;
    }

    public String getIdMatch() {
        return idMatch;
    }

    public SalesDTO setIdMatch(String idMatch) {
        this.idMatch = idMatch;
        return this;
    }

    public String getQuantity() {
        return quantity;
    }

    public SalesDTO setQuantity(String quantity) {
        this.quantity = quantity;
        return this;
    }
}
