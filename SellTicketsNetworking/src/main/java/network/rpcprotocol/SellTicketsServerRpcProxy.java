package network.rpcprotocol;


import entity.Match;
import entity.Sale;
import entity.User;
import exceptions.ControllerException;
import network.dto.DTOUtils;
import network.dto.MatchDTO;
import network.dto.SalesDTO;
import network.dto.UserDTO;
import services.SaleHouseException;
import services.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class SellTicketsServerRpcProxy implements ISellTicketsServer {
    private String host;
    private int port;

    private ISellTicketsClient client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public SellTicketsServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public void login(User user, ISellTicketsClient client) throws SaleHouseException {
        initializeConnection();
        UserDTO udto= DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new SaleHouseException(err);
        }
    }


    public void logout(User user, ISellTicketsClient client) throws SaleHouseException {
        UserDTO udto=DTOUtils.getDTO(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new SaleHouseException(err);
        }
    }



    @Override
    public void sellTickets(String idMatch, String quantity, String buyerPerson) throws SaleHouseException, ServiceException {
        SalesDTO salesDTO = new SalesDTO(idMatch,quantity,buyerPerson);
        Request request = new Request.Builder().type(RequestType.SELL_TICKETS).data(salesDTO).build();
        this.sendRequest(request);
        Response response = this.readResponse();
        if(response.type() == ResponseType.ERROR){
            String msg = (String) response.data();
            throw new ServiceException(msg);
        }
    }


    private List<Match> getAllGeneric(RequestType req) throws ControllerException, SaleHouseException {
        Request request = new Request.Builder().type(req).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String msg = (String) response.data();
            throw new ControllerException(msg);
        }
        List<MatchDTO> artistDTOs = (List<MatchDTO>) response.data();
        return DTOUtils.getMatchesListFromDTO(artistDTOs);
    }

    @Override
    public List<Match> getAllMatches() throws ControllerException, SaleHouseException {
        return getAllGeneric(RequestType.GET_ALL);
    }

    @Override
    public List<Match> getFilteredAndSortedMatches() throws ControllerException, SaleHouseException {
        return getAllGeneric(RequestType.GET_ALL_FILTERED_SORTED);
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws SaleHouseException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new SaleHouseException("Error sending object "+e);
        }

    }

    private Response readResponse() throws SaleHouseException {
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws SaleHouseException {
        try {
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    qresponses.put((Response)response);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e.getMessage());
                } catch (InterruptedException e) {
                   System.out.println("Qresponse queue put error: " + e.getMessage());
                }
            }
        }
    }
}
