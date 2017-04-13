package services;


import entity.Match;
import entity.User;
import exceptions.ControllerException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ISellTicketsServer extends Remote {
    public void login(User user, ISellTicketsClient client) throws SaleHouseException, ControllerException,RemoteException;
    public void logout(User user, ISellTicketsClient client) throws SaleHouseException,RemoteException;
    public void sellTickets(String idMatch,String quantity,String buyerPerson,String username) throws SaleHouseException, ServiceException,RemoteException;

    List<Match> getAllMatches() throws ControllerException, SaleHouseException,RemoteException;
    List<Match> getFilteredAndSortedMatches() throws ControllerException, SaleHouseException,RemoteException;
}