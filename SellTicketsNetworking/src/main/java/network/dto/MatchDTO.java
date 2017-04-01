package network.dto;

import java.io.Serializable;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class MatchDTO implements Serializable{
    private String team1;
    private String team2;
    private String stage;
    private Integer tickets;
    private Integer id;
    private Double price;

    public MatchDTO(String team1, String team2, String stage, Integer tickets, Integer id, Double price) {
        this.team1 = team1;
        this.team2 = team2;
        this.stage = stage;
        this.tickets = tickets;
        this.id = id;
        this.price = price;
    }

    public String getTeam1() {
        return team1;
    }

    public MatchDTO setTeam1(String team1) {
        this.team1 = team1;
        return this;
    }

    public String getTeam2() {
        return team2;
    }

    public MatchDTO setTeam2(String team2) {
        this.team2 = team2;
        return this;
    }

    public String getStage() {
        return stage;
    }

    public MatchDTO setStage(String stage) {
        this.stage = stage;
        return this;
    }

    public Integer getTickets() {
        return tickets;
    }

    public MatchDTO setTickets(Integer tickets) {
        this.tickets = tickets;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public MatchDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public MatchDTO setPrice(Double price) {
        this.price = price;
        return this;
    }
}
