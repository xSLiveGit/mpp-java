package repositories;

import domain.IEntity;
import domain.Ticket;
import org.junit.Before;
import org.junit.Test;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.RepositoryException;
import utils.mapper.TicketMapper;
import utils.validators.ValidatorTicket;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class DatabaseRepositoryTest {
    DatabaseRepository<Ticket> databaseRepository;
    DatabaseConnectionManager databaseConnectionManager;
    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/database.properties"));
        databaseConnectionManager = new DatabaseConnectionManager(properties);
        databaseRepository = new DatabaseRepository<>(databaseConnectionManager,new TicketMapper(),"ticketstest",new ValidatorTicket());
        databaseRepository.clear();
    }

    @Test
    public void getAll() throws Exception {
        Ticket t1 = new Ticket(1,2.0d);
        Ticket t2 = new Ticket(2,3.0d);

        Integer id1 = databaseRepository.add(t1);
        Integer id2 = databaseRepository.add(t2);
        List<Ticket> list = databaseRepository.getAll();
        assertEquals(list.size(),2);
        t1.setId(id1);
        t2.setId(id2);
        assertTrue(list.contains(t1));
        assertTrue(list.contains(t2));


    }

    @Test
    public void findById() throws Exception {
        Integer id = databaseRepository.add(new Ticket(1,2.0d));
        try{
            Ticket el = databaseRepository.findById(id);
            assertEquals(el,new Ticket(id,2.0d));
            assertTrue(true);
        }
        catch (RepositoryException e){
            assertFalse(true);
        }

        try{
            Ticket el = databaseRepository.findById(id-1);
            assertFalse(true);
        }
        catch (RepositoryException e){
            assertTrue(true);
        }

    }

    @Test
    public void update() throws Exception {
        Integer id1 = databaseRepository.add(new Ticket(2.0d));
        databaseRepository.update(new Ticket(id1,3.0d));
        List<Ticket> lst = databaseRepository.getAll();
        assertEquals(lst.size(),1);
        assertTrue(lst.contains(new Ticket(id1,3.0d)));
        assertFalse(lst.contains(new Ticket(id1,2.0d)));

    }

    @Test
    public void add() throws Exception {
        Ticket ticket = new Ticket(3.0d);
        Integer id = databaseRepository.add(ticket);
        Integer size = databaseRepository.getSize();
        assertEquals(size,new Integer(1));
        Integer id2 = databaseRepository.add(new Ticket(2.0d));
        List<Ticket> lst = databaseRepository.getAll();
        assertTrue(lst.contains(new Ticket(id,3.0d)));
        assertTrue(lst.contains(new Ticket(id2,2.0d)));
    }

    @Test
    public void delete() throws Exception {
        Ticket t1 = new Ticket(2.0d);
        Ticket t2 = new Ticket(3.0d);
        Integer id1 = databaseRepository.add(t1);
        Integer id2 = databaseRepository.add(t2);
        List<Ticket> list = databaseRepository.getAll();
        assertEquals(list.size(),2);
        databaseRepository.delete(id1);
        list = databaseRepository.getAll();
        assertEquals(list.size(),1);
        t2.setId(id2);
        t1.setId(id1);
        assertTrue(list.contains(t2));
        assertFalse(list.contains(t1));
    }

    @Test
    public void getSize() throws Exception {

    }

}