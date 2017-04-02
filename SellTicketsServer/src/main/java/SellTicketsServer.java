import entity.Match;
import entity.User;
import exceptions.ControllerException;
import services.SaleHouseException;
import services.ISellTicketsClient;
import services.ISellTicketsServer;
import services.ServiceException;
import services.controller.MatchController;
import services.controller.TicketController;
import services.controller.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class SellTicketsServer implements ISellTicketsServer {
    MatchController matchController = null;
    TicketController ticketController = null;
    UserController userController = null;
    private ArrayList<ISellTicketsClient> clientsList;
    private Map<String,ISellTicketsClient> loggedClients;


    public SellTicketsServer(MatchController matchController, TicketController ticketController, UserController userController) {
        clientsList = new ArrayList<>();
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.userController = userController;
        this.loggedClients = new HashMap<>();
    }

    @Override
    public synchronized void login(User user, ISellTicketsClient client) throws SaleHouseException, ControllerException {
        User u = this.userController.logIn(user.getUsername(),user.getPassword());
        clientsList.add(client);
        loggedClients.put(user.getId(),client);
    }


    @Override
    public synchronized void logout(User user,ISellTicketsClient client) throws SaleHouseException {
        ISellTicketsClient clientRemoved = loggedClients.remove(user.getUsername());
        if(clientRemoved == null){
            throw new SaleHouseException("This user isn't logged in.");
        }
    }

    @Override
    public synchronized void sellTickets(String idMatch, String quantity, String buyerPerson,String username) throws SaleHouseException, ServiceException {
        try {
            this.ticketController.add(idMatch,buyerPerson,quantity);
            Match match = this.matchController.findById(idMatch);
            this.notifyUsersTransaction(match,username);
        } catch (ControllerException e) {
            throw new SaleHouseException(e.getMessage());
        }
    }

    @Override
    public synchronized List<Match> getAllMatches() throws ControllerException {
        return this.matchController.getAll();
    }

    @Override
    public synchronized List<Match> getFilteredAndSortedMatches() throws ControllerException {
        return this.matchController.getAllMatchesWithRemainingTickets();
    }

    private void notifyUsersTransaction(Match match,String usernameC) throws ServiceException{

        int defaultThreadsNumber = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNumber);

        //notifies all clients
        for (String username : loggedClients.keySet()){
            executorService.execute(() -> {
                try {
                    System.out.println(username);
                        if(!usernameC.equals(username)){
                            ISellTicketsClient client = loggedClients.get(username);
                            client.showUpdates(match);
                        }
                } catch (ControllerException e) {
                    System.err.println("Error notifying user");
                }
            });
        }
    }
}
