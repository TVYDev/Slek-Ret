package com.example.tvy.slekret;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TVY on 6/13/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    //Database name
    private static final String DB_NAME = "notesManager";

    //Table names
    private static final String TB_NOTE = "tb_note";
    private static final String TB_BUTTON = "tb_button";
    private static final String TB_IMAGE = "tb_image";

    //Column names of tb_note
    private static final String CL_NOTE_ID = "noteId";
    private static final String CL_NOTE_TITLE = "title";
    private static final String CL_NOTE_DATE = "date";

    //Column names of tb_button
    private static final String CL_BTN_ID = "btnId";
    private static final String CL_BTN_X = "btnX";
    private static final String CL_BTN_Y = "btnY";
    private static final String CL_BTN_TEXT = "text";
    private static final String CL_BTN_TXTSIZE = "txtSize";
    private static final String CL_BTN_TXTALIGN = "txtAlign";
    private static final String CL_BTN_TXTTYPEFACE = "txtTypeface";
    private static final String CL_BTN_TXTFLAG = "txtFlag";
    private static final String CL_BTN_TXTCOLOR = "txtColor";

    //Column names of tb_image
    private static final String CL_IMG_ID = "imgId";
    private static final String CL_IMG_X = "imgX";
    private static final String CL_IMG_Y = "imgY";
    private static final String CL_IMG_WIDTH = "width";
    private static final String CL_IMG_HEIGHT = "height";
    private static final String CL_IMG_SRC = "src";
    private static final String CL_IMG_URI = "uri";


    //Constructor
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
        Log.d(TAG, "DatabaseHandler: ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NOTE + "(" +
                CL_NOTE_ID + " INTEGER PRIMARY KEY," +
                CL_NOTE_TITLE + " TEXT," +
                CL_NOTE_DATE + " TEXT" +
                ")");
        Log.d(TAG, "onCreate: TB_NOTE is created");

        db.execSQL("CREATE TABLE " + TB_BUTTON + "(" +
                CL_BTN_ID + " INTEGER PRIMARY KEY," +
                CL_BTN_X + " INTEGER," +
                CL_BTN_Y + " INTEGER," +
                CL_BTN_TEXT + " TEXT," +
                CL_BTN_TXTSIZE + " INTEGER," +
                CL_BTN_TXTALIGN + " INTEGER," +
                CL_BTN_TXTTYPEFACE + " INTEGER," +
                CL_BTN_TXTFLAG + " INTEGER," +
                CL_BTN_TXTCOLOR + " INTEGER," +
                CL_NOTE_ID + " INTEGER," +
                "FOREIGN KEY(" + CL_NOTE_ID + ") REFERENCES " + TB_NOTE + "(" + CL_NOTE_ID + ")" +
                ")");
        Log.d(TAG, "onCreate: TB_BUTTON is created");

        db.execSQL("CREATE TABLE " + TB_IMAGE + "(" +
                CL_IMG_ID + " INTEGER PRIMARY KEY," +
                CL_IMG_X + " INTEGER," +
                CL_IMG_Y + " INTEGER," +
                CL_IMG_WIDTH + " INTEGER," +
                CL_IMG_HEIGHT + " INTEGER," +
                CL_IMG_SRC + " BLOB," +
//                CL_IMG_URI + " TEXT," +
                CL_NOTE_ID + " INTEGER," +
                "FOREIGN KEY(" + CL_NOTE_ID + ") REFERENCES " + TB_NOTE + "(" + CL_NOTE_ID + ")" +
                ")");
        Log.d(TAG, "onCreate: TB_IMAGE is created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BUTTON);
        db.execSQL("DROP TABLE IF EXISTS " + TB_IMAGE);

        onCreate(db);
        Log.d(TAG, "onUpgrade: ");
    }

    public void deleteAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TB_BUTTON);
        db.execSQL("DROP TABLE IF EXISTS " + TB_IMAGE);
        onCreate(db);
    }

    public void deleteTableNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_NOTE);

        db.execSQL("CREATE TABLE " + TB_NOTE + "(" +
                CL_NOTE_ID + " INTEGER PRIMARY KEY," +
                CL_NOTE_TITLE + " TEXT," +
                CL_NOTE_DATE + " TEXT" +
                ")");
        Log.d(TAG, "onCreate: TB_NOTE is recreated");
    }

    public void deleteTableButton(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TB_BUTTON);

        db.execSQL("CREATE TABLE " + TB_BUTTON + "(" +
                CL_BTN_ID + " INTEGER PRIMARY KEY," +
                CL_BTN_X + " INTEGER," +
                CL_BTN_Y + " INTEGER," +
                CL_BTN_TEXT + " TEXT," +
                CL_BTN_TXTSIZE + " INTEGER," +
                CL_BTN_TXTALIGN + " INTEGER," +
                CL_BTN_TXTTYPEFACE + " INTEGER," +
                CL_BTN_TXTFLAG + " INTEGER," +
                CL_BTN_TXTCOLOR + " INTEGER," +
                CL_NOTE_ID + " INTEGER," +
                "FOREIGN KEY(" + CL_NOTE_ID + ") REFERENCES " + TB_NOTE + "(" + CL_NOTE_ID + ")" +
                ")");
        Log.d(TAG, "onCreate: TB_BUTTON is recreated");
    }

    public void addNote(TableNote note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(CL_NOTE_ID, note.getId());
        content.put(CL_NOTE_TITLE, note.getTitle());
        content.put(CL_NOTE_DATE, note.getDate());

        db.insert(TB_NOTE,null,content);
        db.close();
    }

    public ArrayList<Note_Model> getAllNote()
    {
        ArrayList<Note_Model> note_models = new ArrayList<>();
//        note_models.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TB_NOTE;
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Note_Model noteModel = new Note_Model();
                noteModel.setId(c.getInt(c.getColumnIndex(CL_NOTE_ID)));
                noteModel.setImg(R.drawable.note64);
                noteModel.setDate(c.getString(c.getColumnIndex(CL_NOTE_DATE)));
                noteModel.setTitle(c.getString(c.getColumnIndex(CL_NOTE_TITLE)));
                note_models.add(noteModel);
            }while(c.moveToNext());
        }

        Log.d(TAG, "Get all note");

        return note_models;
    }


    public void addButton(TableButton btn){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(CL_BTN_ID, btn.getId());
        content.put(CL_BTN_X, btn.getX());
        content.put(CL_BTN_Y, btn.getY());
        content.put(CL_BTN_TEXT, btn.getText());
        content.put(CL_BTN_TXTSIZE, btn.getTxtSize());
        content.put(CL_BTN_TXTALIGN, btn.getTxtAlign());
        content.put(CL_BTN_TXTTYPEFACE, btn.getTxtTypeface());
        content.put(CL_BTN_TXTFLAG, btn.getTxtFlag());
        content.put(CL_BTN_TXTCOLOR, btn.getTxtColor());
        content.put(CL_NOTE_ID, btn.getNoteID());

        db.insert(TB_BUTTON, null, content);
        db.close();
    }

    public void addImage(TableImage img){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(CL_IMG_ID, img.getId());
        content.put(CL_IMG_X, img.getImgX());
        content.put(CL_IMG_Y, img.getImgY());
        content.put(CL_IMG_WIDTH, img.getWidth());
        content.put(CL_IMG_HEIGHT, img.getHeight());
        content.put(CL_IMG_SRC, img.getSrc());
//        content.put(CL_IMG_URI, img.getUri());
        content.put(CL_NOTE_ID, img.getNoteId());

        db.insert(TB_IMAGE, null, content);
        db.close();
    }

    public List<TableImage> getImages(int noteId){
        List<TableImage> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TB_IMAGE + " WHERE " + CL_NOTE_ID + "=" + noteId;

        Cursor cursor = db.rawQuery(query, null);

        TableImage img;
        if(cursor.moveToFirst()){
            do{
                img = new TableImage();
                img.setId(cursor.getInt(cursor.getColumnIndex(CL_IMG_ID)));
                img.setImgX(cursor.getInt(cursor.getColumnIndex(CL_IMG_X)));
                img.setImgY(cursor.getInt(cursor.getColumnIndex(CL_IMG_Y)));
                img.setWidth(cursor.getInt(cursor.getColumnIndex(CL_IMG_WIDTH)));
                img.setHeight(cursor.getInt(cursor.getColumnIndex(CL_IMG_HEIGHT)));
                img.setSrc(cursor.getBlob(cursor.getColumnIndex(CL_IMG_SRC)));
//                String st="";
//                for(int i=0;i<cursor.getBlob(cursor.getColumnIndex(CL_IMG_SRC)).length;i++){
//                    st = st + " " + cursor.getBlob(cursor.getColumnIndex(CL_IMG_SRC))[i];
//                }
//                Log.d(TAG, "getImages: " + st);
//                img.setUri(cursor.getString(cursor.getColumnIndex(CL_IMG_SRC)));
                img.setNoteId(cursor.getInt(cursor.getColumnIndex(CL_NOTE_ID)));

                images.add(img);
            }while(cursor.moveToNext());
        }

        return images;
    }

    public TableNote getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TB_NOTE + " WHERE " + CL_NOTE_ID + "=" + id;

        Cursor c = db.rawQuery(query,null);

        if(c != null){
            c.moveToFirst();
        }

        TableNote note = new TableNote();
        note.setId(c.getInt(c.getColumnIndex(CL_NOTE_ID)));
        note.setTitle(c.getString(c.getColumnIndex(CL_NOTE_TITLE)));
        note.setDate(c.getString(c.getColumnIndex(CL_NOTE_DATE)));
        return note;
    }

    public List<Integer> getAllNoteId(){
        List<Integer> lstId = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CL_NOTE_ID + " FROM " + TB_NOTE;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                lstId.add(cursor.getInt(cursor.getColumnIndex(CL_NOTE_ID)));
            }while(cursor.moveToNext());
        }
        return lstId;
    }

    public List<Integer> getAllImagesId(){
        List<Integer> lstImgId = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + CL_IMG_ID + " FROM " + TB_IMAGE ;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                lstImgId.add(cursor.getInt(cursor.getColumnIndex(CL_IMG_ID)));
            }while(cursor.moveToNext());
        }
        return  lstImgId;
    }

    public List<TableButton> getButtons(int id){

        List<TableButton> buttons = new ArrayList<>();

        String query = "SELECT * FROM " + TB_BUTTON + " btn WHERE btn." + CL_NOTE_ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.d(TAG, "getButtons: Cursor " + c.getCount());

        if(c.moveToFirst()){
            do{
                TableButton btn = new TableButton();
                btn.setId(c.getInt(c.getColumnIndex(CL_BTN_ID)));
                btn.setX(c.getInt(c.getColumnIndex(CL_BTN_X)));
                btn.setY(c.getInt(c.getColumnIndex(CL_BTN_Y)));
                btn.setText(c.getString(c.getColumnIndex(CL_BTN_TEXT)));
                btn.setTxtSize(c.getInt(c.getColumnIndex(CL_BTN_TXTSIZE)));
                btn.setTxtAlign(c.getInt(c.getColumnIndex(CL_BTN_TXTALIGN)));
                btn.setTxtTypeface(c.getInt(c.getColumnIndex(CL_BTN_TXTTYPEFACE)));
                btn.setTxtFlag(c.getInt(c.getColumnIndex(CL_BTN_TXTFLAG)));
                btn.setTxtColor(c.getInt(c.getColumnIndex(CL_BTN_TXTCOLOR)));
                btn.setNoteID(c.getInt(c.getColumnIndex(CL_NOTE_ID)));

                buttons.add(btn);
            }while(c.moveToNext());
        }
        return buttons;
    }

    public int updateNote(int noteId, String title, String date){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CL_NOTE_TITLE, title);
        values.put(CL_NOTE_DATE, date);

        return db.update(TB_NOTE, values, CL_NOTE_ID + "=?", new String[]{String.valueOf(noteId)});
    }

    public void deleteButtonsOfNote(int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_BUTTON,CL_NOTE_ID + "=?",new String[]{String.valueOf(noteId)});
        db.close();
    }

    public void deleteImagesOfNote(int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_IMAGE, CL_NOTE_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    public void deleteNote(int noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_NOTE, CL_NOTE_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    public int getButtonCount(int noteId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_BUTTON + " WHERE " + CL_NOTE_ID + "=" + noteId;
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();

        return  count;
    }

    public int getImageCount(int noteId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_IMAGE + " WHERE " + CL_NOTE_ID + "=" + noteId;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getAllButtonCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_BUTTON;
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();

        return  count;
    }

    public int getAllImageCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_IMAGE;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getNoteCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TB_NOTE;
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }



    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }


}
