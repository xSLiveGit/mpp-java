package utils.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Sergiu on 3/11/2017.
 */
public interface IMapper<T> {
    String getIdTextField();
    T toObject(ResultSet row) throws SQLException;
    Map<String,String> toMap(T object);
}
