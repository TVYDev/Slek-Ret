package com.example.tvy.slekret;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Ly PanhaRith on 6/14/2017.
 */

public class Sign_up extends Fragment {

    private TextInputEditText user_acc;
    private TextInputEditText user_pass;
    private TextInputEditText user_email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.sign_up_layout, container, false);



        Button btncancel = (Button)rootview.findViewById(R.id.btn_cancel_frag_sign_up);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fram_container, new Frag_account_setting());
                ft.commit();
            }
        });

        Button btncreate = (Button)rootview.findViewById(R.id.btn_create_frag_sign_up);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_acc=(TextInputEditText)rootview.findViewById(R.id.user_name_frag_sign_up);
                user_pass=(TextInputEditText) rootview.findViewById(R.id.user_password_frag_sign_up);
                user_email=(TextInputEditText)rootview.findViewById(R.id.user_email_frag_sign_up);

                MainActivity.user_acc=user_acc.getText().toString().trim();
                MainActivity.user_pass=user_pass.getText().toString().trim();
                MainActivity.user_email=user_email.getText().toString().trim();

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fram_container, new Frag_account_setting());
                ft.commit();
            }
        });

        return rootview;
    }


}
