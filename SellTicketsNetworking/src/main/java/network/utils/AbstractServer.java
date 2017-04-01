package network.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sergiu on 3/31/2017.
 */
public abstract class AbstractServer {
    //port
    private Integer port;
    //socket
    private ServerSocket serverSocket = null;

    //constructor
    public AbstractServer(Integer port){
        this.port = port;
    }

    //start server
    public void start() throws ServerException{
        try{
            serverSocket = new ServerSocket(port);
            while (true){
                System.out.println("Waiting for clients");

                //recieve client
                Socket socketClient = serverSocket.accept();

                System.out.println("Client connected");

                //process client request
                processRequest(socketClient);
            }
        } catch (IOException e) {
            throw new ServerException("Starting server error");
        } finally {
            try{
                serverSocket.close();
            } catch (IOException e) {
                throw new ServerException("Closing server error");
            }
        }
    }

    //process a client request
    protected abstract void processRequest(Socket socketClient);

    //stops the server process
    public void stop() throws ServerException{
        try{
            serverSocket.close();
        } catch (IOException e) {
            throw new ServerException("Closing server error");
        }
    }

}
