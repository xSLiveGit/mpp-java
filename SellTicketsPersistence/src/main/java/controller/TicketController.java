package controller;


import entity.Ticket;
import exceptions.ControllerException;
import exceptions.RepositoryException;
import repository.MatchRepository;
import repository.TicketRepository;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class TicketController {
    TicketRepository ticketRepository;
    MatchRepository matchRepository;
    public TicketController(TicketRepository ticketRepository,MatchRepository matchRepository) {
        this.ticketRepository = ticketRepository;
        this.matchRepository = matchRepository;
    }

    public Integer add(String idMatch,String person,String quantity) throws ControllerException {
        Integer id = null;
        Integer idMatchI = null;
        Integer quantityI = null;
        try{
            idMatchI = Integer.parseInt(idMatch);
            quantityI = Integer.parseInt(quantity);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid integer.");
        }

        try {
            matchRepository.sellTickets(idMatchI,quantityI);
            id = this.ticketRepository.addId(new Ticket(idMatchI,person,quantityI));

        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Ticket delete(String id) throws ControllerException {
        Integer idI = null;
        try{
            idI = Integer.parseInt(id);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid integer.");
        }
        try {
            return this.ticketRepository.delete(idI);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public void update(String id,String idMatch,String person,String quantity) throws ControllerException {
        Integer idI = null;
        Integer idMatchI = null;
        Integer quantityI = null;
        try{
            idMatchI = Integer.parseInt(idMatch);
            quantityI = Integer.parseInt(quantity);
            idI = Integer.parseInt(id);
        }
        catch (Exception e){
            codeThrowControllerExceptionStatement("Invalid integer.");
        }
        try {
            this.ticketRepository.update(new Ticket(idI,idMatchI,person,quantityI));
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }

    private void codeThrowControllerExceptionStatement(String e) throws ControllerException {
        throw new ControllerException(e);
    }
}
