package com.example.tvy.slekret;

/**
 * Created by TVY on 6/13/2017.
 */

public class TableNote {
    int id;
    String title;
    String date;

    public TableNote() {
    }

    public TableNote(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
