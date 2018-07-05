package Client;

import Server.IRemoteLecture;
import Server.Lecture;

import java.io.IOException;
import java.rmi.Naming;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.*;

/**
 * Client is the class for RMI client. The user will be able to input commands in the console.<br>
 * Valid commands are: help, add, time, lecture, quit.
 * <p>
 * 'help' shows the list of available commands.<br>
 * 'add' allows the client to input lectures.<br>
 * 'time' shows the free time the student has on each day.<br>
 * 'lecture' shows all the already input lectures.<br>
 * 'quit' closes the socket, as well as the server.<br>
 * </p>
 *
 * @author M Chi Nguyen, 1206243
 * @version 27.05.2017
 */
public class Client {

    static IRemoteLecture remote;

    /**
     * Main method, used to start the client
     * @param args String array of main method
     */
    public static void main(String[] args) throws Exception {
        remote = (IRemoteLecture) Naming.lookup("localhost/Service");
        Client client = new Client();
        client.initialize();
    }

    /**
     * This method starts the client, enabling the user to input in the console.
     * Depending which command user inputs, the method will call other methods.<br>
     * Commands are non-case sensitive.
     * @throws Exception
     */
    public void initialize() throws Exception {
        try {
            out.println("Connected to server");
            while (true) {
                out.println("What would you like to do? type 'help' for list of commands");
                Scanner scanner = new Scanner(System.in);
                String data = scanner.nextLine();
                if (data.equalsIgnoreCase("quit")) {
                    remote.quit();
                    break;
                } else if (data.equalsIgnoreCase("help")) {
                    out.println("add     \t to add more lectures in your time table");
                    out.println("lecture \t to show all your lectures you've input");
                    out.println("time    \t to show all your free time in your weekdays");
                    out.println("quit    \t to quit the program");
                } else if (data.equalsIgnoreCase("add")) {
                    out.println("Input format: 'ID Title Weekday HourStart:MinStart' E.G: '01 Math monday 8:00'");
                    add();
                } else if (data.equalsIgnoreCase("lecture")) {
                    getLectures();
                } else if (data.equalsIgnoreCase("time")) {
                    getTime();
                } else {
                    out.println("Command not recognized.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("Server successfully shut down.");
    }

    /**
     * The method is called when the user inputs 'add'. User can then add new lectures with the format
     * 'ID Title Weekday TimeStart(HH:mm)'. <br> Weekday is non-case sensitive.<br> TimeStart has to be between 8:00 and 17:00.
     * <br> If the input are all valid, lecture will be added and send to the server through IRemoteLecture.addLecture()
     * @throws Exception
     */
    public void add() throws Exception {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("stop")) {
            String[] split = input.split("\\s+");
            if (split.length == 4) {
                if (split[2].equalsIgnoreCase("monday") || (split[2].equalsIgnoreCase("tuesday"))
                        || (split[2].equalsIgnoreCase("wednesday")) || (split[2].equalsIgnoreCase("thursday"))
                        || (split[2].equalsIgnoreCase("friday"))) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                        LocalTime time = LocalTime.parse(split[3], formatter);
                        LocalTime minTime = LocalTime.parse("7:59", formatter);
                        LocalTime maxTime = LocalTime.parse("17:01", formatter);
                        if (time.isAfter(minTime) && time.isBefore(maxTime)) {
                            Lecture inputLecture = new Lecture(split[0], split[1], split[2], time);
                            int check = check(split[2], time, inputLecture);
                            if (check == 0) {
                                out.println("Lecture added! Continue adding lecture, 'stop' to stop adding.");
                                remote.addLecture(inputLecture);
                                add();
                            } else if (check == 1) {
                                out.println("Lecture not added! Continue adding lecture, 'stop' to stop adding.");
                                add();
                            } else {
                                out.println("Lecture replaced. Continue adding lecture, 'stop' to stop adding.");
                                add();
                            }
                        } else error();
                    } catch (DateTimeParseException e) { error(); }
                } else error();
            } else error();
        }
    }

    /**
     * The method is called to check if the input lecture is duplicated. If yes the user can choose to overwrite.
     * @param time starting time of the lecture
     * @param lecture the lecture that was input
     * @return an int of 0, 1 or 2.<br> 0 = Not Duplicated. <br> 1 = Duplicated and Not replaced. <br> 2 = Duplicated and Replaced.
     * @throws Exception
     */
    public int check(String day, LocalTime time, Lecture lecture) throws Exception {
        ArrayList<Lecture> lectures = remote.getLectures();
        int timeDuplicate = 0;
        int index = 0;
        if (lectures.size() != 0) {
            for (int i = 0; i < lectures.size(); i++) {
                if (day.equalsIgnoreCase(lectures.get(i).getWeekDay())) {
                    if (time.equals(lectures.get(i).getTime())) {
                        timeDuplicate = 1;
                        index = i;
                    }
                }
            }
            if (timeDuplicate == 1) {
                out.println("Time duplicated, Overwrite? Y/N");
                Scanner scanner1 = new Scanner(System.in);
                String data = scanner1.nextLine();
                if (data.equalsIgnoreCase("y")) {
                    remote.replace(index, lecture);
                    timeDuplicate = 2;
                }
            }
        }
        return timeDuplicate;
    }


    /**
     * Sub-method of addLecture(), called when there's an error. It prints in the console an error message and then
     * tries to repeat the addLecture() for the user to input again.
     * @throws Exception
     */
    public void error() throws Exception {
        out.println("Invalid input! Continue adding lecture, 'stop' to stop adding.");
        add();
    }

    /**
     * The method is called when the user inputs 'lecture'. The method sends a request to the server, in return
     * the server replies with all lectures that were previously input by the user (even from previous sessions).
     *
     * @throws Exception
     */
    public void getLectures() throws Exception{
        ArrayList<Lecture> lectures = remote.getLectures();
        if (lectures.size() != 0) {
            for (int i = 0; i < lectures.size(); i++) {
                out.println(lectures.get(i).toString());
            }
        } else out.println("No lecture found");
    }

    /**
     * The method is called when the user inputs 'time'. The method sends a request to the server, in return
     * the server replies with the free time on each day printed in the client's console.
     *
     * @throws Exception
     */
    public void getTime() throws Exception {
        String[] free = remote.getFreeTime();
        String[] weekDay = {"Monday   ", "Tuesday  ", "Wednesday", "Thursday ", "Friday   "};
        for (int i = 0; i < free.length; i++) {
            out.println(weekDay[i] + " " + free[i]);
        }
    }
}
