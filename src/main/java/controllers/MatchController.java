package controllers;

import domain.Match;
import domain.Ticket;
import repositories.DatabaseRepository;
import repositories.IRepository;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;
import utils.mapper.IMapper;
import utils.mapper.MatchMapper;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class MatchController {
    IRepository<Match,Integer> matchRepository;

    public MatchController(IRepository<Match,Integer> repository) throws RepositoryException {
        this.matchRepository = repository;
    }

    public Integer add(String team1,String team2,String stage,Integer remainingTickets) throws ControllerException {
        Match m = new Match(team1,team2,stage,remainingTickets);
        Integer id = null;
        try {
            id =  matchRepository.add(m);
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Match delete(String id) throws ControllerException {
        try {
            return matchRepository.delete(Integer.parseInt(id));
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public void update(String team1,String team2,String stage,Integer remainingTickets,Integer id) throws ControllerException {
        try {
            matchRepository.update(new Match(id,team1,team2,stage,remainingTickets));
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    public void decreaseNumbersOfTickets(Integer id) throws ControllerException {
        try {
            Match m = matchRepository.findById(id);
            m.decreaseTicketsNumber();
            this.matchRepository.update(m);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }


}
