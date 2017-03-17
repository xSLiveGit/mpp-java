package utils.mapper;

import domain.Sale;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/16/2017.
 */
public class SaleMapper implements  IMapper<Sale> {
    @Override
    public String getIdTextField() {
        return "id";
    }

    @Override
    public Sale toObject(ResultSet row) throws SQLException {
        Sale sale = new Sale();
        sale.setId(row.getInt(1));
        sale.setIdMatch(row.getInt(2));
        sale.setIdTicket(row.getInt(3));
        sale.setPerson(row.getString(4));
        sale.setQuantity(row.getInt(5));
        return sale;
    }

    @Override
    public Map<String, String> toMap(Sale object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("idTicket",object.getIdTicket().toString());
        map.put("idMatch",object.getIdMatch().toString());
        map.put("person",object.getPerson());
        map.put("quantity",object.getQuantity().toString());
        return map;
    }
}
