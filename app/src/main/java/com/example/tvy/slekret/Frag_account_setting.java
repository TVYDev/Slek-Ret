package com.example.tvy.slekret;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ly PanhaRith on 5/29/2017.
 */

public class Frag_account_setting extends Fragment {

    private ImageView user_image_from_navigation;
    private TextView user_name_from_navigation;
    private TextView user_email_from_navigation;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "" + MainActivity.user_acc, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "" + MainActivity.user_pass, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "" + MainActivity.user_email, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.frag_account, container, false);

        final Button btnSignIn = (Button)rootview.findViewById(R.id.btnSignIn);
        final Button btnSignOut = (Button)rootview.findViewById(R.id.btnSignOut);
        final CardView user_info = (CardView) rootview.findViewById(R.id.user_Info_Frag_acc_setting);
        final RelativeLayout question = (RelativeLayout)rootview.findViewById(R.id.question_Frag_account_setting);
        final TextView Sign_Up = (TextView)rootview.findViewById(R.id.sign_up);

        if(MainActivity.log_in_check==true)
        {
            user_info.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            question.setVisibility(View.GONE);
        }else
        {
            user_info.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.GONE);
            question.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        }


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fram_container, new Sign_In());
                ft.commit();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.log_in_check=false;
                user_info.setVisibility(View.GONE);
                btnSignOut.setVisibility(View.GONE);
                question.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
            }
        });
        Sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fram_container, new Sign_up());
                ft.commit();
            }
        });

        return rootview;
    }
}
