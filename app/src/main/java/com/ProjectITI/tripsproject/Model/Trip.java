package com.ProjectITI.tripsproject.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trip {

  //  public static  List<Trip> UpcomingData = new ArrayList<>();
//    public static  List<Trip> HistoryData = new ArrayList<>();

    public Trip(String name, String from, String to, String time, String date, String status, String type, String repeat,boolean wait) {
        Name = name;
        From = from;
        To = to;
        Time = time;
        Date = date;
        this.status = status;
        this.type = type;
        this.repeat = repeat;
        this.wait = wait;
    }

    private String id;

    private String Name;

    private String From;

    private String To;

    private String Time;

    private String Date;

    private String status;

    private String type;

    private String repeat;
    private ArrayList<String> notes;

    private boolean wait=false;

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
    public Trip(String id, String name, String from, String to, String time, String date, String status, String type, String repeat) {
        this.id = id;
        Name = name;
        From = from;
        To = to;
        Time = time;
        Date = date;
        this.status = status;
        this.type = type;
        this.repeat = repeat;
    }
    public Trip(String id, String name, String from, String to, String time, String date, String status, String type, String repeat, ArrayList<String> notes,boolean wait) {
        this.id = id;
        Name = name;
        From = from;
        To = to;
        Time = time;
        Date = date;
        this.status = status;
        this.type = type;
        this.repeat = repeat;
        this.notes = notes;
        this.wait = wait;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }
}
