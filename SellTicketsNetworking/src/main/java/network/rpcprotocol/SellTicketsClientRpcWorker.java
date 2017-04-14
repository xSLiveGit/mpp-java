package network.rpcprotocol;



import entity.Match;
import entity.Sale;
import entity.User;
import exceptions.ControllerException;
import network.dto.DTOUtils;
import network.dto.MatchDTO;
import network.dto.SalesDTO;
import network.dto.UserDTO;
import services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class SellTicketsClientRpcWorker implements Runnable,ISellTicketsClient {
    private ISellTicketsServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private BlockingQueue<Response> blockingQueue;
    private volatile boolean connected;

    public SellTicketsClientRpcWorker(ISellTicketsServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        blockingQueue = new LinkedBlockingDeque<>();
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();

                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
                if(blockingQueue.size() > 0){
                    try {
                        sendResponse(blockingQueue.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }




    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request)  {
        Response response=null;
        RequestType requestType = request.type();
        try{
            switch (requestType){
                case GET_ALL:
                    List<MatchDTO> list = new ArrayList<>();
                    this.server.getAllMatches().forEach(el->list.add(DTOUtils.getDTO(el)));
                    response = new Response.Builder().data(list).type(ResponseType.OK).build();
                    break;

                case GET_ALL_FILTERED_SORTED:
                    List<MatchDTO> list2 = new ArrayList<>();
                    this.server.getFilteredAndSortedMatches().forEach(el->list2.add(DTOUtils.getDTO(el)));
                    response = new Response.Builder().data(list2).type(ResponseType.OK).build();
                    break;

                case SELL_TICKETS:
                    SalesDTO salesDTO = (SalesDTO) request.data();
                    Sale sale = DTOUtils.getFromDTO(salesDTO);
                    this.server.sellTickets(sale.getIdMatch().toString(),sale.getQuantity().toString(),sale.getPerson(),salesDTO.getUsername());
                    response = new Response.Builder().type(ResponseType.OK).build();
                    break;

                case LOGIN:
                    UserDTO user = (UserDTO) request.data();
                    this.server.login(DTOUtils.getFromDTO(user),this);
                    response = new Response.Builder().type(ResponseType.OK).build();
                    break;

                case LOGOUT:
                    UserDTO user2 = (UserDTO) request.data();
                    this.server.logout(DTOUtils.getFromDTO(user2),this);
                    response = new Response.Builder().type(ResponseType.OK).build();
                    break;
            }
        }
       catch (Exception e){
           response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
       }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }


    @Override
    public void showUpdates(Match match) throws ControllerException {
        MatchDTO matchDTO = DTOUtils.getDTO(match);
        Response response = new Response.Builder().type(ResponseType.SHOW_UPDATED_ENTITIES).data(matchDTO).build();
        System.out.println("Worker send update: " + match);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User login(String username, String password) throws SaleHouseException, ControllerException, RemoteException {
        return null;
    }

    @Override
    public void logout() throws SaleHouseException, RemoteException {

    }

    @Override
    public void sellTickets(String idMatch, String quantity, String buyerPerson) throws SaleHouseException, ServiceException, RemoteException {

    }

    @Override
    public List<Match> getAllMatches() throws ControllerException, RemoteException {
        return null;
    }

    @Override
    public List<Match> getFilteredAndSortedMatches() throws ControllerException, RemoteException {
        return null;
    }
}
