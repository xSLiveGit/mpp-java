package utils.mapper;

import domain.Match;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class MatchMapper implements IMapper<Match> {

    @Override
    public String getIdTextField() {
        return "id";
    }

    @Override
    public Match toObject(ResultSet row) throws SQLException {
        Match match = new Match();
        match.setId(row.getInt(1));
        match.setTeam1(row.getString(2));
        match.setTeam2(row.getString(3));
        match.setStage(row.getString(4));
        match.setRemainingTickets(row.getInt(5));
        match.setPrice(row.getDouble(6));
        return match;
    }

    @Override
    public Map<String, String> toMap(Match object) {
        Map<String,String> map = new HashMap<>();
        map.put("id",object.getId().toString());
        map.put("team1", object.getTeam1() );
        map.put("team2", object.getTeam2() );
        map.put("stage",object.getStage() );
        map.put("ticketRemainig",object.getRemainingTickets().toString());
        map.put("price",object.getPrice().toString());
        return map;
    }
}
