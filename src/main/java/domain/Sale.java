package domain;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class Sale implements IEntity<Integer>{
    private Integer idMatch;
    private Integer idTicket;
    private Integer id;
    private String person;
    private Integer quantity;



    public Sale(Integer id, Integer idMatch, Integer idTicket, String person, Integer quantity) {
        this.idMatch = idMatch;
        this.idTicket = idTicket;
        this.person = person;
        this.id = id;
        this.quantity = quantity;
    }

    public Sale(Integer idMatch,Integer idTicket,String person,Integer quantity){
        this(-1,idMatch,idTicket,person,quantity);
    }

    public Sale(){
        this(-1,-1,-1,"",-1);
    }

    public void setIdMatch(Integer idMatch) {
        this.idMatch = idMatch;
    }

    public void setIdTicket(Integer idTicket) {
        this.idTicket = idTicket;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Integer getIdMatch() {
        return idMatch;
    }

    public Integer getIdTicket() {
        return idTicket;
    }

    public String getPerson() {
        return person;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {

        return quantity;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Sale){
            Sale s = Sale.class.cast(obj);
            return this.idMatch.equals(s.idMatch)&&
                    this.idTicket.equals(s.idTicket) &&
                    this.id.equals(s.id) &&
                    this.person.equals(s.person) &&
                    this.quantity.equals(s.quantity);

        }
        return false;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
