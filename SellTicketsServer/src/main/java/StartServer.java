
import exceptions.RepositoryException;
import network.utils.AbstractServer;
import network.utils.SellTicketsRpcConcurrentServer;
import network.utils.ServerException;
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
 * Created by Sergiu on 4/2/2017.
 */
public class StartServer {
    public static void main(String[] args) throws RepositoryException {
        Properties serverProperties = new Properties();
        try {
            serverProperties.load(StartServer.class.getResourceAsStream("./server.properties"));
        } catch (IOException e) {
            System.err.println("Can't find the properties file");
            return;
        }

        DatabaseConnectionManager databaseConnectionManager = new DatabaseConnectionManager(serverProperties);

        MatchRepository matchRepository = new MatchRepository(databaseConnectionManager,"matches",new ValidatorMatch());
        TicketRepository ticketRepository = new TicketRepository(databaseConnectionManager,"tickets",new ValidatorTicket());
        ValidatorUser validatorUser = new ValidatorUser();
        UserRepository userRepository = new UserRepository(databaseConnectionManager,"users",validatorUser);

        MatchController matchController = new MatchController(matchRepository);
        TicketController ticketController = new TicketController(ticketRepository,matchRepository);
        UserController userController = new UserController(userRepository);

        ISellTicketsServer festivalServer = new SellTicketsServer(matchController,ticketController,userController);
        int chatServerPort = 0;
        try {
            chatServerPort = Integer.parseInt(serverProperties.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.out.println("Wrong port format. Using default port instead " + chatServerPort);
        }

        AbstractServer server = new SellTicketsRpcConcurrentServer(chatServerPort, festivalServer);
        try {
            server.start();
        } catch (ServerException e) {
            System.out.println("Error starting server");
        }
    }
}
