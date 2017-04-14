package network.utils;



import network.rpcprotocol.SellTicketsClientRpcWorker;
import services.ISellTicketsServer;

import java.net.Socket;

/**
 * Created by Sergiu on 3/31/2017.
 */
public class SellTicketsRpcConcurrentServer extends AbstractConcurrentServer {
    //server over festival app services
    private ISellTicketsServer server;

    //constructor
    public SellTicketsRpcConcurrentServer(Integer port, ISellTicketsServer server) {
        super(port);
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket socketClient) {
        SellTicketsClientRpcWorker worker = new SellTicketsClientRpcWorker(server, socketClient);
        Thread tw = new Thread(worker);
        return tw;
    }
}
