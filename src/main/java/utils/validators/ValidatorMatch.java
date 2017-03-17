package utils.validators;

import domain.Match;
import utils.exceptions.RepositoryException;

import java.util.ArrayList;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class ValidatorMatch implements IValidator<Match> {
    private ArrayList<String> errList;

    public ValidatorMatch(){
        errList = new ArrayList<>();
    }

    private void validateId(Integer id){
        if(id <= 0){
            errList.add("Id must be positive numbers.");
        }
    }

    private void validateTeamName(String teamName,Integer teamIndex){
        if("".equals(teamName)){
            errList.add(String.format("Team with index %d has no name.",teamIndex));
        }
    }

    private void validateTicketsNumber(Integer ticketsNumber){
        if(ticketsNumber < 0){
            errList.add("The numbers of tickets must be greater than 0.");
        }
    }



    private void validateStage(String stage){
        if(!stage.equals("Final") && !stage.startsWith("Semifinal ") && !stage.startsWith("Group ")){
            errList.add("Stage is invalid. Stage must be \"Final\", \"Semifinal x\" or \"Group x\" where x is a big letter.");
        }

    }

    @Override
    public void validate(Match el) throws RepositoryException {
        errList.clear();
        validateTeamName(el.getTeam1(),1);
        validateTeamName(el.getTeam2(),2);
        validateTicketsNumber(el.getRemainingTickets());
        if(errList.size() != 0){
            throw new RepositoryException(errList.toString());
        }
    }
}
