package services;


import entity.Match;
import entity.User;
import exceptions.ControllerException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ISellTicketsClient extends Remote{
     void showUpdates(Match match) throws ControllerException,RemoteException;

     User login(String username, String password) throws SaleHouseException, ControllerException, RemoteException;

     void logout() throws SaleHouseException, RemoteException;

     void sellTickets(String idMatch, String quantity, String buyerPerson) throws SaleHouseException, ServiceException, RemoteException;

     List<Match> getAllMatches() throws ControllerException, RemoteException ;

     List<Match> getFilteredAndSortedMatches() throws ControllerException,RemoteException;
}
