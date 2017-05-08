
import exceptions.RepositoryException;
import repository.*;
import services.ISellTicketsServer;
import controller.MatchController;
import controller.TicketController;
import controller.UserController;
import utils.DatabaseConnectionManager;
import utils.validator.ValidatorMatch;
import utils.validator.ValidatorTicket;
import utils.validator.ValidatorUser;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
        DataAccessObject dataAccessObject = new DataAccessObject();
        MatchRepository matchRepository = new MatchRepository(dataAccessObject,new ValidatorMatch());
        TicketRepository ticketRepository = new TicketRepository(dataAccessObject,new ValidatorTicket());
        ValidatorUser validatorUser = new ValidatorUser();
        UserRepository userRepository = new UserRepository(new ValidatorUser());

        MatchController matchController = new MatchController(matchRepository);
        TicketController ticketController = new TicketController(ticketRepository,matchRepository);
        UserController userController = new UserController(userRepository);

        ISellTicketsServer sellTicketsServer = new SellTicketsServer(matchController,ticketController,userController);
//        ISellTicketsServer sellTicketsServer = new SellTicketsServer();

        try{
            String name = "SellTickets";
            System.setProperty("java.rmi.server.hostname","127.0.0.1");
            LocateRegistry.createRegistry(1099);
            ISellTicketsServer stub = (ISellTicketsServer) UnicastRemoteObject.exportObject(sellTicketsServer,0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,stub);
            System.out.println("SellTicketsServer bound");
        }catch (Exception e){
            System.err.println("SellTicketsServerException : " + e);
            e.printStackTrace();
        }
    }
}
