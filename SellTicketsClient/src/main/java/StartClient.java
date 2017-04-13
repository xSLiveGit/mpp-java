import controller.ClientController;
import gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import services.ISellTicketsServer;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

/**
 * Created by Sergiu on 3/30/2017.
 */
public class StartClient extends Application {
    private static String defaultServer="127.0.0.1";
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        Properties clientProps=new Properties();
        try{
            String name = "SellTickets";
            Registry registry = LocateRegistry.getRegistry(defaultServer);
            ISellTicketsServer server = (ISellTicketsServer)registry.lookup(name);
            System.out.println("Obtained a reference to a remote sell tickets server!");
            ClientController clientController = new ClientController(server,null);//
            GUI gui = new GUI(clientController);
            gui.start();
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
