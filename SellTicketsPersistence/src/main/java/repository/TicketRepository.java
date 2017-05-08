package repository;


import entity.Match;
import entity.Ticket;
import exceptions.RepositoryException;
import utils.validator.ValidatorTicket;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class TicketRepository implements IRepository<Ticket,Integer> {
    DataAccessObject<Ticket,Integer> dao;
    ValidatorTicket validator;
    public TicketRepository(DataAccessObject<Ticket, Integer> dao,ValidatorTicket validator) {
        this.dao = dao;
        this.validator = validator;
    }

    @Override
    public List<Ticket> getAll() throws RepositoryException {
        return this.dao.getAll(Ticket.class,"tickets");
    }

    @Override
    public Ticket findById(Integer integer) throws RepositoryException {
        return this.dao.findById(Ticket.class,integer);
    }

    @Override
    public void update(Ticket element) throws RepositoryException {
        validator.validate(element);
        this.dao.update(Ticket.class,element);
    }

    @Override
    public Integer add(Ticket element) throws RepositoryException, SQLException {
        validator.validate(element);
        return this.dao.add(Ticket.class,element);
    }

    @Override
    public Ticket delete(Integer integer) throws RepositoryException {
        return this.dao.delete(Ticket.class,integer);
    }

    @Override
    public Integer getSize() throws RepositoryException, SQLException {
        return this.dao.getSize("tickets");
    }

    @Override
    public void clear() throws RepositoryException {

    }
}
