import controllers.MatchController;
import controllers.TicketController;
import controllers.UserController;
import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import repositories.MatchRepository;
import repositories.TicketRepository;
import repositories.UserRepository;
import utils.database.DatabaseConnectionManager;
import utils.validators.ValidatorMatch;
import utils.validators.ValidatorTicket;
import utils.validators.ValidatorUser;

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
            MatchRepository matchRepository = new MatchRepository(databaseManager,"matches",new ValidatorMatch());
            TicketRepository ticketRepository = new TicketRepository(databaseManager,"tickets",new ValidatorTicket());

            MatchController matchController = new MatchController(matchRepository);
            TicketController ticketController = new TicketController(ticketRepository,matchRepository);

            ValidatorUser validatorUser = new ValidatorUser();
            UserRepository userRepository = new UserRepository(databaseManager,"users",validatorUser);

            UserController userController = new UserController(userRepository);
            GUI gui = new GUI(matchController,ticketController,databaseManager,userController);
            gui.start();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
