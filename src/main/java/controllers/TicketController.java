package controllers;

import domain.Ticket;
import repositories.IRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class TicketController {
    IRepository<Ticket,Integer> ticketRepository;

    public TicketController(IRepository<Ticket, Integer> ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Integer add(Double price) throws ControllerException {
        Integer id = null;
        try {
            id = this.ticketRepository.add(new Ticket(price));
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Ticket delete(Integer id) throws ControllerException {
        try {
            return this.ticketRepository.delete(id);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public void update(Integer id,Double price) throws ControllerException {
        try {
            this.ticketRepository.update(new Ticket(id,price));
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }
}
