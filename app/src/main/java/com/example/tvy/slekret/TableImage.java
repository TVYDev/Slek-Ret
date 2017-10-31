package com.example.tvy.slekret;

/**
 * Created by TVY on 6/16/2017.
 */

public class TableImage {

    int id;
    int imgX;
    int imgY;
    int width;
    int height;
    byte[] src;
    String uri;
    int noteId;

    public TableImage() {
    }

    public TableImage(int id, int imgX, int imgY, int width, int height, byte[] src, String uri, int noteId) {
        this.id = id;
        this.imgX = imgX;
        this.imgY = imgY;
        this.width = width;
        this.height = height;
        this.src = src;
        this.uri = uri;
        this.noteId = noteId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgX() {
        return imgX;
    }

    public void setImgX(int imgX) {
        this.imgX = imgX;
    }

    public int getImgY() {
        return imgY;
    }

    public void setImgY(int imgY) {
        this.imgY = imgY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[] getSrc() {
        return src;
    }

    public void setSrc(byte[] src) {
        this.src = src;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
