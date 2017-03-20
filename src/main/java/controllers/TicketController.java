package controllers;

import domain.Ticket;
import repositories.IDatabaseRepository;
import repositories.IRepository;
import utils.exceptions.ControllerException;
import utils.exceptions.RepositoryException;

import java.sql.SQLException;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class TicketController {
    IDatabaseRepository<Ticket,Integer> ticketRepository;

    public TicketController(IDatabaseRepository<Ticket, Integer> ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Integer add(Double price,boolean startTransaction,boolean endTransaction) throws ControllerException {
        Integer id = null;
        try {
            id = this.ticketRepository.add(new Ticket(price),startTransaction,endTransaction);
        } catch (RepositoryException | SQLException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return id;
    }

    public Ticket delete(Integer id,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        try {
            return this.ticketRepository.delete(id,startTransaciton,endTransaction);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
        return null;
    }

    public void update(Integer id,Double price,boolean startTransaciton,boolean endTransaction) throws ControllerException {
        try {
            this.ticketRepository.update(new Ticket(id,price),startTransaciton,endTransaction);
        } catch (RepositoryException e) {
            codeThrowControllerExceptionStatement(e);
        }
    }

    private void codeThrowControllerExceptionStatement(Exception e) throws ControllerException {
        throw new ControllerException(e);
    }
}
