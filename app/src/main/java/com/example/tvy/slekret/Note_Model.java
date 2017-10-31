package com.example.tvy.slekret;

import android.media.Image;
import android.widget.ImageView;

import java.util.Comparator;

/**
 * Created by Ly PanhaRith on 6/1/2017.
 */

public class Note_Model {

    public int id;
    public int Img;
    public String Date;
    public String Title;


    public Note_Model()
    {

    }

    public Note_Model(int img, String date, String title)
    {
        Img = img;
        Date = date;
        Title = title;
    }

    public Note_Model(int id, int img, String date, String title) {
        this.id = id;
        Img = img;
        Date = date;
        Title = title;
    }

    public String getDate()
    {
        return Date;
    }
    public void setDate(String Date) { this.Date=Date; }

    public int getId(){ return  id; }
    public void setId(int id){ this.id=id; }

    public String getTitle()
    {
        return Title;
    }
    public void setTitle(String Title)
    {
        this.Title=Title;
    }

    public int getImg()
    {
        return Img;
    }
    public void setImg(int Img)
    {
        this.Img=Img;
    }


}
