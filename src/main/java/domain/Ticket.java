package domain;

import java.sql.Time;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class Ticket implements IEntity<Integer> {
    private Integer id;
    private Double price;

    public Ticket(Integer id, Double price) {
        this.id = id;
        this.price = price;
    }

    public Ticket() {

    }

    public Ticket(Double price){
        this(-1,price);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Ticket){
            Ticket t = (Ticket)obj;
            return this.id.equals(t.id) &&
                    this.price.equals(t.price);
        }
        return false;
    }
}
