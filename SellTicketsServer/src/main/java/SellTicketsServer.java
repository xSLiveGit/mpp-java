import entity.Match;
import entity.Ticket;
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
import java.util.List;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class SellTicketsServer implements ISellTicketsServer {
    MatchController matchController = null;
    TicketController ticketController = null;
    UserController userController = null;
    private ArrayList<ISellTicketsClient> clientsList;

    public SellTicketsServer(MatchController matchController, TicketController ticketController, UserController userController) {
        clientsList = new ArrayList<>();
        this.matchController = matchController;
        this.ticketController = ticketController;
        this.userController = userController;
    }

    @Override
    public synchronized void login(User user, ISellTicketsClient client) throws SaleHouseException, ControllerException {
        User u = this.userController.logIn(user.getUsername(),user.getPassword());
        clientsList.add(client);
    }


    @Override
    public void logout(User user,ISellTicketsClient client) throws SaleHouseException {
        boolean succesLogout = clientsList.remove(client);
        if(!succesLogout){
            throw new SaleHouseException("This user isn't logged in.");
        }
    }

    @Override
    public void sellTickets(String idMatch, String quantity, String buyerPerson) throws SaleHouseException, ServiceException {
        try {
            this.ticketController.add(idMatch,buyerPerson,quantity);
        } catch (ControllerException e) {
            throw new SaleHouseException(e.getMessage());
        }
    }

    @Override
    public List<Match> getAllMatches() throws ControllerException {
        return this.matchController.getAll();
    }

    @Override
    public List<Match> getFilteredAndSortedMatches() throws ControllerException {
        return this.matchController.getAllMatchesWithRemainingTickets();
    }
}
