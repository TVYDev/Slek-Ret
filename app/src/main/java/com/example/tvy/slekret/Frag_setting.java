package com.example.tvy.slekret;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ly PanhaRith on 5/29/2017.
 */

public class Frag_setting extends Fragment {

    private String enable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.frag_setting, container, false);

//        final ArrayList<String> list = new ArrayList<>();
//        list.add("FONT 1");
//        list.add("font 2");
//        list.add("FoNt 3");
//
//        //Spinner spinner = (Spinner)rootview.findViewById(R.id.font_spinner);
//
//        final ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this.getContext().getApplicationContext()
//                ,R.layout.spinner_data_model
//                ,list);
//
//        Adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
//
//        //spinner.setAdapter(Adapter);
//
//        //on spinner item select
//        //spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                // On selecting a spinner item
//                String item = list.get(position);
//
//                Toast.makeText(getContext(),
//                        "id : "+id , Toast.LENGTH_SHORT).show();
//
//                Toast.makeText(getContext(),
//                        "position : "+position , Toast.LENGTH_SHORT).show();
//
//
//                if(item.equals("FONT 1"))
//                {
//                    Toast.makeText(getContext(),
//                                "Selected  : 0", Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(getContext(),
//                            "Selected font : "+item , Toast.LENGTH_SHORT).show();
//
//                }else if(item.equals("font 2"))
//                {
//                    Toast.makeText(getContext(),
//                                "Selected  : 1", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(),
//                            "Selected font : "+item , Toast.LENGTH_SHORT).show();
//                }else
//                {
//                    Toast.makeText(getContext(),
//                                "Selected  : 2" , Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(),
//                            "Selected font : "+item , Toast.LENGTH_SHORT).show();
//                }
//
//                //int item = Integer.parseInt(list.get(position));
////                switch(item){
////                    case 0:
////                        Toast.makeText(getContext(),
////                                "Selected Country : 0", Toast.LENGTH_SHORT).show();
////                        break;
////                    case 1:
////                        Toast.makeText(getContext(),
////                                "Selected Country : 1", Toast.LENGTH_SHORT).show();
////                        break;
////                    case 2:
////                        Toast.makeText(getContext(),
////                                "Selected Country : 2" , Toast.LENGTH_SHORT).show();
////                        break;
////
////                }
//                // Showing selected spinner item
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        Switch ShowPreviewPhoto = (Switch)rootview.findViewById(R.id.switch_PreView_Photo);
        Switch AutoSync = (Switch)rootview.findViewById(R.id.switch_sync_automatic);

        ShowPreviewPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false) {

                    MainActivity.value="False";
                    Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                }else
                {
                    MainActivity.value="True";
                    Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AutoSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==false)
                {

                    Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        enable=MainActivity.value;
        if(enable=="True")
        {
            ShowPreviewPhoto.setChecked(true);
        }else{
            ShowPreviewPhoto.setChecked(false);
        }
        return rootview;
    }
}
