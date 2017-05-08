package entity;


import exceptions.EntityArgumentException;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Sergiu on 3/11/2017.
 */

@Entity
@Table(name = "matches")
public class Match implements IEntity<Integer>,Serializable{
    @Column(name = "team1")
    private String team1;

    @Column(name = "team2")
    private String team2;

    @Column(name = "stage")
    private String stage;

    @Column(name = "tickets")
    private Integer tickets;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "id")
    private Integer id;

    @Column(name = "price")
    private Double price;

//    @OneToMany(fetch = FetchType.LAZY,mappedBy="matches")
//    private Set<Ticket> allTickets;
//
//    public Set<Ticket> getAllTickets() {
//        return allTickets;
//    }

    public Match(Integer id, String team1, String team2, String stage, Integer tickets, Double price) throws EntityArgumentException {
        this.team1 = team1;
        this.team2 = team2;
        this.stage = stage;
        this.tickets = tickets;
        this.id = id;
        this.price = price;
        if(tickets < 0){
            throw new EntityArgumentException("The number of tickets must be >=0.");
        }
    }

    public Match(String team1, String team2, String stage, Integer tickets, Double price) throws EntityArgumentException {
        this(-1,team1,team1,stage, tickets,price);
    }

    public void decreaseTicketsNumber(){
        this.tickets--;
    }

    public Match() throws EntityArgumentException {
        this(-1,"","","",0,0d);
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Match setTickets(Integer tickets) throws EntityArgumentException {
        if(tickets < 0){
            throw new EntityArgumentException("The number of tickets must be >=0.");
        }
        this.tickets = tickets;
        return this;
    }

    public String getTicketsString() throws EntityArgumentException {
        if(tickets < 0){
            throw new EntityArgumentException("The number of tickets must be >=0.");
        }
        if(tickets == 0)
            return "SOLD OUT";
        return tickets.toString();
    }

    public String getTeam1() {

        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getStage() {
        return stage;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {

        return price;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (!team1.equals(match.team1)) return false;
        if (!team2.equals(match.team2)) return false;
        if (!stage.equals(match.stage)) return false;
        if (!tickets.equals(match.tickets)) return false;
        if (!id.equals(match.id)) return false;
        return price.equals(match.price);
    }

    @Override
    public int hashCode() {
        int result = team1.hashCode();
        result = 31 * result + team2.hashCode();
        result = 31 * result + stage.hashCode();
        result = 31 * result + tickets.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }
}
