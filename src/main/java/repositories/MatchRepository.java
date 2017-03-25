package repositories;

import domain.Match;
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
public class MatchRepository extends AbstractDatabaseRepositoryId<Match> {

    public MatchRepository(DatabaseConnectionManager dbConnManager, String tableName, IValidator<Match> validator) throws RepositoryException {
        super(dbConnManager, tableName, validator);
    }

    @Override
    protected String getIdTextField() {
        return "id";
    }

    @Override
    protected Match toObject(ResultSet row) throws SQLException, EntityArgumentException {
        Match match = new Match();
        match.setId(row.getInt(1));
        match.setTeam1(row.getString(2));
        match.setTeam2(row.getString(3));
        match.setStage(row.getString(4));
        match.setTickets(row.getInt(5));
        match.setPrice(row.getDouble(6));
        return match;
    }

    @Override
    protected Map<String, String> toMap(Match object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("team1", object.getTeam1() );
        map.put("team2", object.getTeam2() );
        map.put("stage",object.getStage() );
        map.put("tickets",object.getTickets().toString());
        map.put("price",object.getPrice().toString());
        return map;
    }

    public void sellTickets(Integer id,Integer quantity) throws RepositoryException {
        String querry = String.format("UPDATE %s SET tickets = (tickets - ?) WHERE id = ?",tableName);
        try {
            PreparedStatement statement = connection.prepareStatement(querry);
            statement.setInt(1,quantity);
            statement.setInt(2,id);
            Integer a = statement.executeUpdate();
            System.out.print("a:" + a + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
            codeThrowRepositoryException(e);
        }

    }
}
