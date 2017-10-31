package com.example.tvy.slekret;

/**
 * Created by TVY on 6/13/2017.
 */

public class TableButton {
    int id;
    int x;
    int y;
    String text;
    int txtSize;
    int txtAlign;
    int txtTypeface;
    int txtFlag;
    int txtColor;
    int noteID;

    public TableButton() {
    }

    public TableButton(int id, int x, int y, String text, int txtSize, int txtAlign, int txtTypeface, int txtFlag, int txtColor, int noteID) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.text = text;
        this.txtSize = txtSize;
        this.txtAlign = txtAlign;
        this.txtTypeface = txtTypeface;
        this.txtFlag = txtFlag;
        this.txtColor = txtColor;
        this.noteID = noteID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    public int getTxtAlign() {
        return txtAlign;
    }

    public void setTxtAlign(int txtAlign) {
        this.txtAlign = txtAlign;
    }

    public int getTxtTypeface() {
        return txtTypeface;
    }

    public void setTxtTypeface(int txtTypeface) {
        this.txtTypeface = txtTypeface;
    }

    public int getTxtFlag() {
        return txtFlag;
    }

    public void setTxtFlag(int txtFlag) {
        this.txtFlag = txtFlag;
    }

    public int getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }
}
