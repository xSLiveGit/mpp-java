package services;


import entity.Match;
import exceptions.ControllerException;

public interface ISellTicketsClient {
    public void showUpdates(Match match) throws ControllerException;
}
