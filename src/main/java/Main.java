import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import utils.database.DatabaseConnectionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class Main {
    public static void main(String argv[]){
        Properties properties = new Properties();
        DatabaseConnectionManager databaseManager;
        try {
            properties.load(Main.class.getClass().getResourceAsStream("/database.properties"));;
            databaseManager = new DatabaseConnectionManager(properties);
            Connection connection = databaseManager.getConnection();
            System.out.print(connection.isClosed());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
