package entity;

import java.io.Serializable;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class Ticket implements IEntity<Integer> ,Serializable {
    private Integer id;
    private String person;
    private Integer quantity;
    private Integer idMatch;

    public Ticket(Integer id, Integer idMatch,String person, Integer quantity) {
        this.id = id;
        this.person = person;
        this.quantity = quantity;
        this.idMatch = idMatch;
    }

    public Ticket( Integer idMatch,String person,Integer quantity) {
        this(0,idMatch,person,quantity);
    }

    public Ticket() {

    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setIdMatch(Integer idMatch) {
        this.idMatch = idMatch;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getIdMatch() {
        return idMatch;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + person.hashCode();
        result = 31 * result + quantity.hashCode();
        result = 31 * result + idMatch.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;

        Ticket ticket = (Ticket) o;

        if (!id.equals(ticket.id)) return false;
        if (!person.equals(ticket.person)) return false;
        if (!quantity.equals(ticket.quantity)) return false;
        return idMatch.equals(ticket.idMatch);
    }


}
