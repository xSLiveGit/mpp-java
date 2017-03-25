package repositories;

import domain.Ticket;
import utils.database.DatabaseConnectionManager;
import utils.exceptions.EntityArgumentException;
import utils.exceptions.RepositoryException;
import utils.validators.IValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class TicketRepository extends AbstractDatabaseRepositoryId<Ticket> {
    public TicketRepository(DatabaseConnectionManager dbConnManager, String tableName, IValidator<Ticket> validator) throws RepositoryException {
        super(dbConnManager, tableName, validator);
    }

    @Override
    protected String getIdTextField() {
        return "id";
    }

    @Override
    protected Ticket toObject(ResultSet row) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(row.getInt(1));
        ticket.setIdMatch(row.getInt(2));
        ticket.setPerson(row.getString(3));
        ticket.setQuantity(row.getInt(4));
        return ticket;
    }

    @Override
    protected Map<String, String> toMap(Ticket object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("idMatch",object.getIdMatch().toString());
        map.put("person",object.getPerson());
        map.put("quantity",object.getQuantity().toString());
        return map;
    }


}
