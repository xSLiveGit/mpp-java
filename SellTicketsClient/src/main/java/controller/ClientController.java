package controller;

import entity.Match;
import entity.User;
import exceptions.ControllerException;
import gui.OperationGUIController;
import services.ISellTicketsClient;
import services.ISellTicketsServer;
import services.SaleHouseException;
import services.ServiceException;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sergiu on 4/1/2017.
 */
public class ClientController implements ISellTicketsClient {

    ISellTicketsServer server = null;
    User user = null;
    OperationGUIController operationGUIController = null;

    public ClientController(ISellTicketsServer server,OperationGUIController operationGUIController) {
        this.operationGUIController = operationGUIController;
        this.server = server;
    }

    public User login(String username, String password) throws SaleHouseException, ControllerException {
        User userL = new User(username, password);
        server.login(userL, this);
        user = userL;
        return  user;
    }


    public void logout() throws SaleHouseException {
        this.server.logout(user,this);
        user = null;
    }

    public void sellTickets(String idMatch, String quantity, String buyerPerson) throws SaleHouseException, ServiceException {
        this.server.sellTickets(idMatch,quantity,buyerPerson,user.getUsername());
    }

    public List<Match> getAllMatches() throws ControllerException {
        try {
            return this.server.getAllMatches();
        } catch (SaleHouseException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    public List<Match> getFilteredAndSortedMatches() throws ControllerException {
        try {
            return this.server.getFilteredAndSortedMatches();
        } catch (SaleHouseException e) {
            throw new ControllerException(e.getMessage());
        }
    }

    public void register(String username,String paswword){
        //TODO
    }

    public void addMatch(String team1,String team2,String stage,String noTickets,String price){
        //TODO
    }

    public void updateMatch(String id,String team1,String team2,String stage,String noTickets,String price){
        //TODO
    }

    public void deleteMatch(String id){
        //TODO
    }

    @Override
    public void showUpdates(Match match) throws ControllerException {
        System.out.println("Client server : Notified" + operationGUIController);
        if(null!= operationGUIController){
            System.out.println("Client server : Notified will be done");
            operationGUIController.update(match);
        }
    }

    public void setOperationGUIController(OperationGUIController operationGUIController){
        System.out.println("Setted controller");
        this.operationGUIController = operationGUIController;
    }
//    @Override
//    public void showUpdates(Match match) throws ControllerException {
//        System.out.println("Client server : Notified");
//        this.notifyObservers(match);
//    }
}
