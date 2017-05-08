package utils.validator;

import entity.Match;
import exceptions.RepositoryException;

import java.util.ArrayList;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class ValidatorMatch implements IValidator<Match> {
    private ArrayList<String> errList;

    public ValidatorMatch(){
        errList = new ArrayList<>();
    }

    public void validateTeam(String team)
    {
        if(team.equals(""))
            errList.add("Team can't be empty.");
        if(team.length() > 30)
            errList.add("The length of team must be <= 30");
    }

    public void validateStage(String stage)
    {
        if(stage.equals(""))
            errList.add("Stage can't be empty string.");
        if(stage.length() > 30)
            errList.add("The length of stage must be <= 30");
        if ( !stage.startsWith("Final") && !stage.startsWith("Semifinal ") && !stage.startsWith("Group "))
        {
            errList.add("Stage is invalid. Stage must be \"Final\", \"Semifinal x\" or \"Group x\" where x is a big letter.");
        }
    }

    public void validatePrice(Double price)
    {
        if(price <= 0)
            errList.add("The price must be positive integer.");
    }

    public void validateTicketsNumber(Integer ticketsNumber)
    {
        if(ticketsNumber < 0)
            errList.add("Number of tickets must be non-negative integer.");
    }

    @Override
    public void validate(Match item) throws RepositoryException {
        errList.clear();
        validateTeam(item.getTeam1());
        validateTeam(item.getTeam2());
        validateStage(item.getStage());
        validatePrice(item.getPrice());
        validateTicketsNumber(item.getTickets());
        if(errList.size() != 0){
            throw new RepositoryException(errList.toString());
        }
    }
}
