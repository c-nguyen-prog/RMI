package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Server is the class that starts the RMI server.
 * @author M Chi Nguyen, 1206243
 * @version 27.05.2017
 */
public class Server {
    public static void main(String[] args) throws MalformedURLException, RemoteException {
        LocateRegistry.createRegistry(1099);
        RemoteLectureImpl remoteLecture = new RemoteLectureImpl();
        remoteLecture.init();
        Naming.rebind("localhost/Service", remoteLecture);
        System.out.println("Server initialized");
    }
}
