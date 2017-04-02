import controller.ClientController;
import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import network.rpcprotocol.SellTicketsClientRpcWorker;
import network.rpcprotocol.SellTicketsServerRpcProxy;
import services.ISellTicketsServer;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Sergiu on 3/30/2017.
 */
public class StartClient extends Application {
    private static int defaultChatPort=55555;
    private static String defaultServer="localhost";
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Properties clientProps=new Properties();
        try{
            clientProps.load(StartClient.class.getResourceAsStream("./database.properties"));
            String serverIp = clientProps.getProperty("server.host");
            Integer serverPort = null;
            try{
                serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
            } catch (NumberFormatException ex){
                System.out.println("Invalid port. Using default instead.");
            }

            System.out.println("Using ip " + serverIp);
            System.out.println("Using port " + serverPort);

            ISellTicketsServer server = new SellTicketsServerRpcProxy(serverIp,serverPort);
            ClientController clientController = new ClientController(server,null);

            GUI gui = new GUI(clientController);
            gui.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
