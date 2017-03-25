package repositories;

import domain.Ticket;
import org.junit.Before;
import org.junit.Test;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.RepositoryException;
import utils.mapper.TicketMapper;
import utils.validators.ValidatorTicket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class AbstractDatabaseRepositoryTest {
    @Test
    public void getItemsByProperty() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("price","2");
        Ticket ticket = new Ticket(3.0d);
        Integer id = abstractDatabaseRepository.add(ticket,true,true);
        Integer size = abstractDatabaseRepository.getSize();
        assertEquals(size,new Integer(1));
        Integer id2 = abstractDatabaseRepository.add(new Ticket(2.0d),true,true);
        List<Ticket> lst = abstractDatabaseRepository.getItemsByProperty(map);
        assertEquals(lst.size(),1);
        assertTrue(lst.contains(new Ticket(id2,2d)));
        assertFalse(lst.contains(new Ticket(3d)));
        Integer id3 = abstractDatabaseRepository.add(new Ticket(2.0d),true,true);
        lst = abstractDatabaseRepository.getItemsByProperty(map);
        assertEquals(lst.size(),2);
        assertTrue(lst.contains(new Ticket(id2,2d)));
        assertFalse(lst.contains(new Ticket(id3,3d)));
    }

    AbstractDatabaseRepository<Ticket> abstractDatabaseRepository;
    DatabaseConnectionManager databaseConnectionManager;
    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/database.properties"));
        databaseConnectionManager = new DatabaseConnectionManager(properties);
        abstractDatabaseRepository = new AbstractDatabaseRepository<>(databaseConnectionManager,new TicketMapper(),"ticketstest",new ValidatorTicket());
        abstractDatabaseRepository.clear();
    }

    @Test
    public void getAll() throws Exception {
        Ticket t1 = new Ticket(1,2.0d);
        Ticket t2 = new Ticket(2,3.0d);

        Integer id1 = abstractDatabaseRepository.add(t1,true,true);
        Integer id2 = abstractDatabaseRepository.add(t2,true,true);
        List<Ticket> list = abstractDatabaseRepository.getAll();
        assertEquals(list.size(),2);
        t1.setId(id1);
        t2.setId(id2);
        assertTrue(list.contains(t1));
        assertTrue(list.contains(t2));


    }

    @Test
    public void findById() throws Exception {
        Integer id = abstractDatabaseRepository.add(new Ticket(1,2.0d),true,true);
        try{
            Ticket el = abstractDatabaseRepository.findById(id,true,true);
            assertEquals(el,new Ticket(id,2.0d));
            assertTrue(true);
        }
        catch (RepositoryException e){
            assertFalse(true);
        }

        try{
            Ticket el = abstractDatabaseRepository.findById(id-1,true,true);
            assertFalse(true);
        }
        catch (RepositoryException e){
            assertTrue(true);
        }

    }

    @Test
    public void update() throws Exception {
        Integer id1 = abstractDatabaseRepository.add(new Ticket(2.0d),true,true);
        abstractDatabaseRepository.update(new Ticket(id1,3.0d),true,true);
        List<Ticket> lst = abstractDatabaseRepository.getAll();
        assertEquals(lst.size(),1);
        assertTrue(lst.contains(new Ticket(id1,3.0d)));
        assertFalse(lst.contains(new Ticket(id1,2.0d)));

    }

    @Test
    public void add() throws Exception {
        Ticket ticket = new Ticket(3.0d);
        Integer id = abstractDatabaseRepository.add(ticket,true,true);
        Integer size = abstractDatabaseRepository.getSize();
        assertEquals(size,new Integer(1));
        Integer id2 = abstractDatabaseRepository.add(new Ticket(2.0d),true,true);
        List<Ticket> lst = abstractDatabaseRepository.getAll();
        assertTrue(lst.contains(new Ticket(id,3.0d)));
        assertTrue(lst.contains(new Ticket(id2,2.0d)));
    }

    @Test
    public void delete() throws Exception {
        Ticket t1 = new Ticket(2.0d);
        Ticket t2 = new Ticket(3.0d);
        Integer id1 = abstractDatabaseRepository.add(t1,true,true);
        Integer id2 = abstractDatabaseRepository.add(t2,true,true);
        List<Ticket> list = abstractDatabaseRepository.getAll();
        assertEquals(list.size(),2);
        abstractDatabaseRepository.delete(id1,true,true);
        list = abstractDatabaseRepository.getAll();
        assertEquals(list.size(),1);
        t2.setId(id2);
        t1.setId(id1);
        assertTrue(list.contains(t2));
        assertFalse(list.contains(t1));
        assertTrue(true);//test
    }

    @Test
    public void getSize() throws Exception {

    }

}