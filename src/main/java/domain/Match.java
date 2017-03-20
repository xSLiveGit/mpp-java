package domain;


/**
 * Created by Sergiu on 3/11/2017.
 */
public class Match implements IEntity<Integer>{
    private String team1;
    private String team2;
    private String stage;
    private Integer remainingTickets;
    private Integer id;
    private Double price;

    public Match(Integer id,String team1, String team2, String stage, Integer remainingTickets,Double price) {
        this.team1 = team1;
        this.team2 = team2;
        this.stage = stage;
        this.remainingTickets = remainingTickets;
        this.id = id;
        this.price = price;
    }

    public Match(String team1,String team2,String stage,Integer remainingTickets,Double price){
        this(-1,team1,team1,stage,remainingTickets,price);
    }

    public void decreaseTicketsNumber(){
        this.remainingTickets--;
    }

    public Match() {
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

    public void setRemainingTickets(Integer remainingTickets) {
        this.remainingTickets = remainingTickets;
    }

    public String getTicketsString(){
        if(remainingTickets <= 0)
            return "SOLD OUT";
        return remainingTickets.toString();
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

    public Integer getRemainingTickets() {
        return remainingTickets;
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
    public boolean equals(Object obj) {
        if(obj instanceof Match){
            Match m = Match.class.cast(obj);
            return m.id.equals(this.id) &&
                    m.team1.equals(this.team1) &&
                    m.team2.equals(this.team2) &&
                    m.remainingTickets.equals(this.remainingTickets);
        }
        return false;
    }
}
