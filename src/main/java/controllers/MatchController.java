package controllers;

import domain.Match;
import repositories.IDatabaseRepository;
import repositories.IRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class MatchController {
    IDatabaseRepository<Match,Integer> matchRepository;

    public MatchController(IDatabaseRepository<Match,Integer> repository) throws RepositoryException {
        this.matchRepository = repository;
    }

    public Integer add(String team1,String team2,String stage,String remainingTickets,String price,boolean startTransaction,boolean endTransaction) throws ControllerException {
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
            if(remainingTicketsI <= 0){
                codeThrowControllerExceptionStatement("Invalid tickets number.");
            }
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid tickets numer.");

        }
        Match m = new Match(team1,team2,stage,remainingTicketsI,priceC);

        Integer id = null;
        try {
            id =  matchRepository.add(m,startTransaction,endTransaction);
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Match delete(String id,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        try {
            return matchRepository.delete(Integer.parseInt(id),startTransaciton, endTransaction);
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
            if(remainingTicketsI < 0){
                codeThrowControllerExceptionStatement("Invalid tickets number.");
            }
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid tickets numer.");

        }
        try {
            matchRepository.update(new Match(id,team1,team2,stage,remainingTicketsI,priceC),startTransaciton,endTransaction);
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

    public void increaseQuantity(Integer idMatch,Integer quantity,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        try {
            Match m =matchRepository.findById(idMatch,startTransaciton,false);
            m.setRemainingTickets(m.getRemainingTickets() + quantity);
            matchRepository.update(m,false,endTransaction);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    public List<Match> getAllMatchesWithRemainingTickets() throws ControllerException {
        try {
            return matchRepository.getAll()
                    .stream().filter(el->el.getRemainingTickets() > 0)
                    .sorted(Comparator.comparingInt(Match::getRemainingTickets).reversed())
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }
}
