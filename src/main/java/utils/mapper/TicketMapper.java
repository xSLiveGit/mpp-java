package utils.mapper;

import domain.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class TicketMapper implements IMapper<Ticket> {
    @Override
    public String getIdTextField() {
        return "id";
    }

    @Override
    public Ticket toObject(ResultSet row) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(row.getInt(1));
        ticket.setPrice(row.getDouble(2));
        return ticket;
    }

    @Override
    public Map<String, String> toMap(Ticket object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("price",object.getPrice().toString());
        return map;
    }
}
