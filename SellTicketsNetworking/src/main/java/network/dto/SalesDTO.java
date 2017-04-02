package network.dto;

import java.io.Serializable;

/**
 * Created by Sergiu on 3/30/2017.
 */
public class SalesDTO implements Serializable {
    private String idMatch = null;
    private String quantity = null;
    private String person = null;
    private String username = null;

    public SalesDTO(String idMatch, String quantity,String person,String username) {
        this.idMatch = idMatch;
        this.quantity = quantity;
        this.person = person;
        this.username = username;
    }

    public SalesDTO setPerson(String person) {
        this.person = person;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SalesDTO setUsername(String username) {
        this.username = username;
        return this;
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
