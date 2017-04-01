package services;


import entity.Match;
import entity.User;
import exceptions.ControllerException;

import java.util.List;

public interface ISellTicketsServer {
    public void login(User user, ISellTicketsClient client) throws SaleHouseException, ControllerException;
    public void logout(User user, ISellTicketsClient client) throws SaleHouseException;
    public void sellTickets(String idMatch,String quantity,String buyerPerson) throws SaleHouseException, ServiceException;

    List<Match> getAllMatches() throws ControllerException, SaleHouseException;
    List<Match> getFilteredAndSortedMatches() throws ControllerException, SaleHouseException;
}