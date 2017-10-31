package com.example.tvy.slekret;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

/**
 * Created by Ly PanhaRith on 6/16/2017.
 */

public class Sign_In extends Fragment{

    private TextInputEditText user_acc;
    private TextInputEditText user_pass;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.sign_in_layout, container, false);

        Button btnSignIn = (Button)rootview.findViewById(R.id.btn_signIn_Frag_sign_in);
        Button btnCancel = (Button)rootview.findViewById(R.id.btn_cancel_Frag_sign_in);
        user_acc=(TextInputEditText)rootview.findViewById(R.id.input_user_name_frag_sign_in);
        user_pass=(TextInputEditText)rootview.findViewById(R.id.input_password_frag_sign_in);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fram_container, new Frag_account_setting());
                ft.commit();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!Objects.equals(user_acc.getText().toString().trim(), MainActivity.user_acc))
                {
                    user_acc.setHintTextColor(Color.RED);
                    user_acc.setHint("Wrong User Account");

                }else if (!Objects.equals(user_pass.getText().toString().trim(), MainActivity.user_pass))
                {
                    user_pass.setHintTextColor(Color.RED);
                    user_pass.setHint("Wrong User Password");
                }else
                {
                    MainActivity.log_in_check=true;

                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fram_container, new Frag_account_setting());
                    ft.commit();
                }
            }
        });

        user_acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user_acc.setHint("");
            }
        });

        user_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                user_pass.setHint("");
            }
        });

        return rootview;
    }
}
