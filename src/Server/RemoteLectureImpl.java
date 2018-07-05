package Server;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class implements the interface IRemoteLecture will functional methods.
 *
 * @author M Chi Nguyen, 1206243
 * @version 27.05.2017
 */
public class RemoteLectureImpl extends UnicastRemoteObject implements IRemoteLecture {
    ArrayList<Lecture> lecture = new ArrayList<>();
    String[] free = new String[5];

    /**
     * Constructor
     * @throws RemoteException
     */
    public RemoteLectureImpl() throws RemoteException {
        super();
    }

    /**
     * This method is called when the user input 'add'. A lecture will be added to the ArrayList.
     * @param lecture the to be added lecture
     * @throws RemoteException
     */
    public void addLecture(Lecture lecture) throws RemoteException {
        this.lecture.add(lecture);
    }

    /**
     * This method is called when the user chooses to overwrite a lecture
     * @param index position of lecture in the ArrayList
     * @param lecture the lecture to be added
     * @throws RemoteException
     */
    public void replace(int index, Lecture lecture) throws RemoteException {
        this.lecture.set(index, lecture);
    }

    /**
     * This method is called when the user input 'lecture'.
     * @return return all lectures that were added.
     * @throws RemoteException
     */
    public ArrayList<Lecture> getLectures() throws RemoteException {
        return lecture;
    }

    /**
     * This method is called when the user input 'time'.
     * @return return free time of the user in a String array.
     * @throws RemoteException
     */
    public String[] getFreeTime() throws RemoteException {
        for (int i = 0; i < free.length; i++) {
            free[i] = "10";
        }
        for (int i = 0; i < lecture.size(); i++) {
            String weekDay = lecture.get(i).getWeekDay();
            if (weekDay.equalsIgnoreCase("monday")) {
                free[0] = Integer.toString(Integer.parseInt(free[0]) - 1);
            } else if (weekDay.equalsIgnoreCase("tuesday")) {
                free[1] = Integer.toString(Integer.parseInt(free[1]) - 1);
            } else if (weekDay.equalsIgnoreCase("wednesday")) {
                free[2] = Integer.toString(Integer.parseInt(free[2]) - 1);
            } else if (weekDay.equalsIgnoreCase("thursday")) {
                free[3] = Integer.toString(Integer.parseInt(free[3]) - 1);
            } else if (weekDay.equalsIgnoreCase("friday")) {
                free[4] = Integer.toString(Integer.parseInt(free[4]) - 1);
            }
        }
        return free;
    }

    /**
     * This method is called when the client input 'quit'. It will save all the lectures into lectures.txt and then shutdown the server.
     * @throws RemoteException
     */
    public void quit() throws RemoteException {
        try {
            OutputStream outputStream2 = new FileOutputStream("lectures.txt");
            PrintStream printStream2 = new PrintStream(outputStream2);
            for (int i = 0; i < lecture.size(); i++) {
                String id = lecture.get(i).getId();
                String title = lecture.get(i).getTitle();
                String weekDay = lecture.get(i).getWeekDay();
                String time = lecture.get(i).getTime().toString();
                printStream2.println(id + " " + title + " " + weekDay + " " + time);
            }
            Naming.unbind("localhost/Service");
            UnicastRemoteObject.unexportObject(this, true);
        } catch (Exception e) {}
    }

    /**
     * This method is called when the server is starting. It will read the lectures.txt and add lectures into the ArrayList.
     * @throws RemoteException
     */
    public void init() throws RemoteException {
        try {
            File file = new File("lectures.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String data = scanner.nextLine();
                String[] split = data.split("\\s+");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(split[3], formatter);
                Lecture lecture = new Lecture(split[0], split[1], split[2], time);
                this.lecture.add(lecture);
            }
        } catch (FileNotFoundException e) {}
    }
}
