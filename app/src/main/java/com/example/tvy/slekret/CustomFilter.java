package com.example.tvy.slekret;

import android.widget.Filter;

import java.util.ArrayList;

/**
 * Created by Ly PanhaRith on 6/20/2017.
 */

public class CustomFilter extends Filter {

    Recyler_View_Adapter adapter;
    ArrayList<Note_Model> filterList;

    public CustomFilter(ArrayList<Note_Model> filterList, Recyler_View_Adapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Note_Model> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.note= (ArrayList<Note_Model>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }

}
