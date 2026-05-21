package com.example.reminderapp.models;

/**
 * Reminder data model.
 * Fields aligned with README: id, title, date, time.
 * (Fixed: original APK only had 'message', missing 'title')
 */
public class Reminder {
    private int id;
    private String title;
    private String date;   // stored as "dd/MM/yyyy"
    private String time;   // stored as "HH:mm"

    public Reminder() {}

    public Reminder(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
