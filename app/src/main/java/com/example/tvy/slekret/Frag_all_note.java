package com.example.tvy.slekret;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.tvy.slekret.NoteActivity.mContext;

/**
 * Created by Ly PanhaRith on 5/29/2017.
 */

public class Frag_all_note extends Fragment {

    DatabaseHandler db;
    private static final int REQUEST_CODE_NOTE_ACTIVITY = 333;
    ArrayList<Note_Model> note_models;
    //String title;
    private String check;
    public Frag_all_note() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //title= MainActivity.note_name;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        db=new DatabaseHandler(getContext());
//        Log.d(TAG, "recyler adarpter");
        View rootview = inflater.inflate(R.layout.frag_all_note, container, false);

        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootview.findViewById(R.id.note_recycler_view);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        CharSequence date = android.text.format.DateFormat.format("dd-MM-yyyy", new java.util.Date());
//
//        note_models.add(new Note_Model(R.mipmap.ic_note_add_black_48dp, (String) date,title));

        //sort by Title
        ArrayList<Note_Model> note_sorted = db.getAllNote();

        Collections.sort(note_sorted, new Comparator<Note_Model>(){
            public int compare(Note_Model obj1, Note_Model obj2) {
                // ## Ascending order
                return obj1.getTitle().compareToIgnoreCase(obj2.getTitle());
            }
        });

        //sort by Date
//        Collections.sort(note_sorted, new Comparator<Note_Model>(){
//            public int compare(Note_Model obj1, Note_Model obj2) {
//                // ## Ascending order
//                return obj1.getDate().compareToIgnoreCase(obj2.getDate());
//            }
//        });

        // 3. create an adapter
        Recyler_View_Adapter Adapter = new Recyler_View_Adapter(note_sorted,getContext());
        // 4. set adapter
        recyclerView.setAdapter(Adapter);

        db.getAllNote().clear();


        check = MainActivity.value;

        if (check=="False")
        {
            Toast.makeText(getContext(), ""+check, Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getContext(), ""+check, Toast.LENGTH_SHORT).show();
        }


       return rootview;
    }

}
