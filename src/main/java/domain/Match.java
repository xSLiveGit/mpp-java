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

    public Match(Integer id,String team1, String team2, String stage, Integer remainingTickets) {
        this.team1 = team1;
        this.team2 = team2;
        this.stage = stage;
        this.remainingTickets = remainingTickets;
        this.id = id;
    }

    public Match(String team1,String team2,String stage,Integer remainingTickets){
        this(-1,team1,team1,stage,remainingTickets);
    }

    public void decreaseTicketsNumber(){
        this.remainingTickets--;
    }

    public Match() {
        this(-1,"","","",0);
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
