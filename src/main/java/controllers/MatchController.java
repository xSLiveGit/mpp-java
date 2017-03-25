package controllers;

import domain.Match;
import repositories.IDatabaseRepository;
import repositories.MatchRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.EntityArgumentException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class MatchController {
    MatchRepository matchRepository;

    public MatchController(MatchRepository repository) throws RepositoryException {
        this.matchRepository = repository;
    }

    public Integer add(String team1,String team2,String stage,String remainingTickets,String price) throws ControllerException, EntityArgumentException {
        Double priceC = null;
        Integer remainingTicketsI = null;
        try{
            priceC = Double.parseDouble(price);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid price.");
        }

        try {
            remainingTicketsI = Integer.parseInt(remainingTickets);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid tickets numer.");

        }
        Match m = new Match(team1,team2,stage,remainingTicketsI,priceC);

        Integer id = null;
        try {
            id =  matchRepository.addId(m);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Match delete(String id) throws ControllerException {
        Integer idI = null;
        try{
            idI = Integer.parseInt(id);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement(e);
        }
        try {
            return matchRepository.delete(idI);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public void update(Integer id,String team1,String team2,String stage,String remainingTickets,String price,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        Double priceC = null;
        Integer remainingTicketsI = null;
        try{
            priceC = Double.parseDouble(price);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid price.");
        }

        try {
            remainingTicketsI = Integer.parseInt(remainingTickets);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid tickets numer.");

        }
        try {
            matchRepository.update(new Match(id,team1,team2,stage,remainingTicketsI,priceC));
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e.getMessage());
    }

    private void codeThrowControllerExceptionStatement(String e) throws ControllerException {
        throw new ControllerException(e);
    }

    public List<Match> getAll() throws ControllerException {
        List<Match> list = null;
        try {
            list = matchRepository.getAll();
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return list;
    }


    public List<Match> getAllMatchesWithRemainingTickets() throws ControllerException {
        try {
            return matchRepository.getAll()
                    .stream().filter(el->el.getTickets() > 0)
                    .sorted(Comparator.comparingInt(Match::getTickets).reversed())
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }
}
