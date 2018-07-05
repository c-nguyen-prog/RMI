package Server;

import java.io.Serializable;
import java.time.LocalTime;

/**
 *
 * @author M Chi Nguyen, 1206243
 * @version 27.05.2017
 */
public class Lecture implements Serializable{
    private String id;
    private String title;
    private String weekDay;
    private LocalTime time;
    /**
     * Constructor
     * @param id the id of the lecture
     * @param title the title of the lecture
     * @param weekDay must be valid weekday, i.e monday, tuesday, wednesday, thursday and friday.
     * @param time starting time of a lecture. Must be between 8:00 and 17:00
     */
    public Lecture(String id, String title, String weekDay, LocalTime time) {
        this.id = id;
        this.title = title;
        this.weekDay = weekDay;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * toString() method
     * @return output the lecture in this following format:
     * <p>ID: id <br>Title: <br>Weekday: <br>Time start:</p>
     */
    public String toString() {
        String output = "ID:         " + this.id;
        output +=     "\nTitle:      " + this.title;
        output +=     "\nWeek day:   " + this.weekDay;
        output +=     "\nTime start: " + this.time.toString() + "\n";
        return output;
    }
}
