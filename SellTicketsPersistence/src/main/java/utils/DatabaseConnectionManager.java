package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class DatabaseConnectionManager {
    private Properties properties;
    private Connection connection;

    public DatabaseConnectionManager(Properties properties){
        this.properties = properties;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(null == connection){
            Class.forName(properties.getProperty("JDBC_DRIVER"));
            String DB_URL = properties.getProperty("DB_URL");
            String USER = properties.getProperty("USER");
            String PASS = properties.getProperty("PASS");
            this.connection = DriverManager.getConnection(DB_URL,USER,PASS);
            connection.setAutoCommit(false);
        }
        return connection;

    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();
        super.finalize();
    }
}
