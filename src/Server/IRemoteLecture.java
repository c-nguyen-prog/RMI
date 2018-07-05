package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * The interface class that is used as middle ground between Client and Server.
 *
 * @author M Chi Nguyen, 1206243
 * @version 27.05.2017
 */
public interface IRemoteLecture extends Remote {
    void addLecture(Lecture lecture) throws RemoteException;
    ArrayList<Lecture> getLectures() throws RemoteException;
    String[] getFreeTime() throws RemoteException;
    void replace(int index, Lecture lecture) throws RemoteException;
    void quit() throws RemoteException;
    void init() throws RemoteException;
}
