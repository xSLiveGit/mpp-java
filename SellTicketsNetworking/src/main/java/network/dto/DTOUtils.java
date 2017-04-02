package network.dto;

import entity.Match;
import entity.Sale;
import entity.User;
import exceptions.EntityArgumentException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 3/30/2017.
 */
public class DTOUtils {
    public static User getFromDTO(UserDTO usdto){
        String id=usdto.getId();
        String pass=usdto.getPasswd();
        return new User(id, pass);

    }
    public static UserDTO getDTO(User user){
        String id=user.getId();
        String pass=user.getPassword();
        return new UserDTO(id, pass);
    }

    public static SalesDTO getDTO(Sale sale,String username){
        return new SalesDTO(sale.getIdMatch().toString(),sale.getQuantity().toString(),sale.getPerson(),username);
    }

    public static Sale getFromDTO(SalesDTO salesDTO){
        return new Sale(Integer.parseInt(salesDTO.getIdMatch()),Integer.parseInt(salesDTO.getQuantity()),salesDTO.getPerson());
    }

    public static MatchDTO getDTO(Match match){
        Integer id = match.getId();
        String team1 =  match.getTeam1();
        String team2 = match.getTeam2();
        String stage = match.getStage();
        Integer tickets = match.getTickets();
        Double price = match.getPrice();
        return new MatchDTO(team1,team2,stage,tickets,id,price);
    }

    public static Match getFromDTO(MatchDTO matchDTO)  {
        Integer id = matchDTO.getId();
        String team1 =  matchDTO.getTeam1();
        String team2 = matchDTO.getTeam2();
        String stage = matchDTO.getStage();
        Integer tickets = matchDTO.getTickets();
        Double price = matchDTO.getPrice();
        try {
            return new Match(id,team1,team2,stage,tickets,price);
        } catch (EntityArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserDTO[] getDTO(User[] users){
        UserDTO[] frDTO=new UserDTO[users.length];
        for(int i=0;i<users.length;i++)
            frDTO[i]=getDTO(users[i]);
        return frDTO;
    }

    public static User[] getFromDTO(UserDTO[] users){
        User[] friends=new User[users.length];
        for(int i=0;i<users.length;i++){
            friends[i]=getFromDTO(users[i]);
        }
        return friends;
    }

    public static List<Match> getMatchesListFromDTO(List<MatchDTO> list){
        List<Match> listM = new ArrayList<>();
        list.forEach(el->listM.add(getFromDTO(el)));
        return listM;
    }


}
