import controllers.MatchController;
import controllers.SaleController;
import controllers.TicketController;
import controllers.UserController;
import domain.Match;
import domain.Sale;
import domain.Ticket;
import domain.User;
import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import repositories.DatabaseRepository;
import utils.database.DatabaseConnectionManager;
import utils.mapper.MatchMapper;
import utils.mapper.SaleMapper;
import utils.mapper.TicketMapper;
import utils.mapper.UserMapper;
import utils.validators.ValidatorMatch;
import utils.validators.ValidatorSale;
import utils.validators.ValidatorTicket;
import utils.validators.ValidatorUser;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Sergiu on 3/11/2017.
 */
public class Main extends Application{
    public static void main(String argv[]){
        launch(argv);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties properties = new Properties();
        DatabaseConnectionManager databaseManager;
        try {
            properties.load(Main.class.getClass().getResourceAsStream("/database.properties"));;
            databaseManager = new DatabaseConnectionManager(properties);
            Connection connection = databaseManager.getConnection();
            System.out.print(connection.isClosed());
            DatabaseRepository<Match> matchDatabaseRepository = new DatabaseRepository<>(databaseManager,new MatchMapper(),"matches",new ValidatorMatch());
            DatabaseRepository<Ticket> ticketDatabaseRepository = new DatabaseRepository<>(databaseManager,new TicketMapper(),"tickets",new ValidatorTicket());
            DatabaseRepository<Sale> saleDatabaseRepository = new DatabaseRepository<>(databaseManager,new SaleMapper(),"sale",new ValidatorSale());

            MatchController matchController = new MatchController(matchDatabaseRepository);
            TicketController ticketController = new TicketController(ticketDatabaseRepository);
            SaleController saleController = new SaleController(saleDatabaseRepository);

            ValidatorUser validatorUser = new ValidatorUser();
            DatabaseRepository<User> databaseRepository = new DatabaseRepository<User>(databaseManager,new UserMapper(),"users",validatorUser);

            UserController userController = new UserController(databaseRepository);
            GUI gui = new GUI(matchController,ticketController,saleController,databaseManager,userController);
            gui.start();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
