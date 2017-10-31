package com.example.tvy.slekret;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ly PanhaRith on 6/1/2017.
 */

public class Recyler_View_Adapter extends RecyclerView.Adapter<Recyler_View_Adapter.ViewHolder> implements Filterable{

    ArrayList<Note_Model> note;
    private Context mcontext;
    private ArrayList<Note_Model> filterList;
    CustomFilter filter;
    DatabaseHandler db;

    public Recyler_View_Adapter(ArrayList<Note_Model> note, Context mcontext)
    {
        this.note=note;
        this.mcontext = mcontext;
        this.filterList=note;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {
        public ImageView img;
        public TextView title;
        public TextView date;
        public TextView popup;
//        public TextView idd;

        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            img =(ImageView)itemView.findViewById(R.id.note_image);
            title=(TextView)itemView.findViewById(R.id.note_title);
            date=(TextView)itemView.findViewById(R.id.note_date);
            popup=(TextView)itemView.findViewById(R.id.textViewOptions);
//            idd = (TextView)itemView.findViewById(R.id.note_id);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public  void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener =itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return false;
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context=parent.getContext();
        LayoutInflater  inflater = LayoutInflater.from(context);

        db = new DatabaseHandler(context);

        View contactview  = inflater.inflate(R.layout.note_model,parent,false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(contactview);
        return (ViewHolder) viewHolder;

    }

    @Override
    public void onBindViewHolder(final Recyler_View_Adapter.ViewHolder holder, final int position) {

        final Note_Model note_model1 = note.get(position);

        ImageView img1 = holder.img;
        img1.setImageResource(note_model1.getImg());

        TextView title1 = holder.title;
        title1.setText(note_model1.getTitle());

        TextView date1 = holder.date;
        date1.setText(note_model1.getDate());

//        TextView id1 = holder.idd;
//        id1.setText(note_model1.getId());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, final int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(mcontext, "Long Click :" + note.get(position), Toast.LENGTH_SHORT).show();
                    PopupMenu popupMenu = new PopupMenu(mcontext, holder.popup);
                    popupMenu.inflate(R.menu.option_menu_for_all_note);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.action_edit:
//                                    Toast.makeText(mcontext, "Edit", Toast.LENGTH_SHORT).show();
                                    break;

                                case R.id.action_delete:
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext,R.style.AppTheme_Blue_Dialog)
                                            .setTitle("Warning")
                                            .setMessage("Are you sure you want to delete this?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    db.deleteNote(note.get(position).getId());
                                                    db.deleteButtonsOfNote(note.get(position).getId());
                                                    db.deleteImagesOfNote(note.get(position).getId());
                                                    note.remove(position);
                                                    notifyDataSetChanged();

//                                                    Toast.makeText(mcontext, "Delete item " + position, Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertDialog = alertDialogBuilder.show();
                                    break;

                                case R.id.action_sync:
                                    Toast.makeText(mcontext, "Sync", Toast.LENGTH_SHORT).show();
                                    break;


//                                case R.id.action_property:
//                                    Toast.makeText(mcontext, "Property", Toast.LENGTH_SHORT).show();
//                                    break;

//                                case R.id.action_property:
////                                    Toast.makeText(mcontext, "Property", Toast.LENGTH_SHORT).show();
//                                    break;

                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }
                else {
//                    Toast.makeText(mcontext, "click :hhhhhhhhhhhhhh" + note.get(position).getId(), Toast.LENGTH_SHORT).show();
                    Intent noteIntent = new Intent(mcontext,NoteActivity.class);
                    noteIntent.putExtra("ID_KEY",note.get(position).getId());
                    noteIntent.putExtra("TITLE_KEY",note.get(position).getTitle());
                    mcontext.startActivity(noteIntent);
                }
            }
        });

        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mcontext, holder.popup);
                popupMenu.inflate(R.menu.option_menu_for_all_note);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_edit:
                                Intent noteIntent = new Intent(mcontext,NoteActivity.class);
                                noteIntent.putExtra("ID_KEY",note.get(position).getId());
                                noteIntent.putExtra("TITLE_KEY",note.get(position).getTitle());
                                mcontext.startActivity(noteIntent);
//                                Toast.makeText(mcontext, "Edit", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_delete:
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext,R.style.AppTheme_Blue_Dialog)
                                .setTitle("Warning")
                                                .setMessage("Are you sure you want to delete this?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        db.deleteNote(note.get(position).getId());
                                                        db.deleteButtonsOfNote(note.get(position).getId());
                                                        db.deleteImagesOfNote(note.get(position).getId());
                                                        note.remove(position);
                                                        notifyDataSetChanged();

//                                                        Toast.makeText(mcontext, "Delete item " + position, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alertDialog = alertDialogBuilder.show();
                                break;

                            case R.id.action_sync:
                                Toast.makeText(mcontext, "Sync", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return note.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }
        return filter;
    }
}

