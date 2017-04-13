import exceptions.RepositoryException;
import repository.MatchRepository;
import repository.TicketRepository;
import repository.UserRepository;
import services.ISellTicketsServer;
import services.controller.MatchController;
import services.controller.TicketController;
import services.controller.UserController;
import utils.DatabaseConnectionManager;
import utils.validator.ValidatorMatch;
import utils.validator.ValidatorTicket;
import utils.validator.ValidatorUser;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sergiu on 4/13/2017.
 */
public class Persistence{
    private static Persistence instance=null;
    private static Properties serverProperties = null;
    MatchController matchController;
    TicketController ticketController;
    UserController userController ;
    DatabaseConnectionManager databaseConnectionManager = null;
    private Persistence(){
        try {
            if(null == serverProperties){
                Properties serverProperties = new Properties();
                try {
                    serverProperties.load(Persistence.class.getResourceAsStream("./server.properties"));
                } catch (IOException e) {
                    System.err.println("Can't find the properties file");
                    return;
                }
                DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager(serverProperties);
                MatchRepository matchRepository = new MatchRepository(databaseConnectionManager,"matches",new ValidatorMatch());
                TicketRepository ticketRepository = new TicketRepository(databaseConnectionManager,"tickets",new ValidatorTicket());
                ValidatorUser validatorUser = new ValidatorUser();
                UserRepository userRepository = new UserRepository(databaseConnectionManager,"users",validatorUser);

                 matchController = new MatchController(matchRepository);
                 ticketController = new TicketController(ticketRepository,matchRepository);
                 userController = new UserController(userRepository);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
    public static Persistence getInstance(){
        if (instance==null){
           instance = new Persistence();
        }
        return instance;
    }

    public UserController createUserController(){
        return userController;
    }

    public MatchController createMatchController() throws RepositoryException {
        return matchController;
    }

    public TicketController createTicketController(){
        return ticketController;
    }

}

