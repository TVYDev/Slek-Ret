package com.example.tvy.slekret;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
//    String note_name;
    Context mContext;
    public static String note_name;
    private EditText Note_Name_From_User;
    private boolean check=false;
    static String  value ;
    static String user_acc;
    static String user_pass;
    static String user_email;
    static Boolean log_in_check=false;


    android.support.v7.widget.SearchView sv;


    private static final int REQUEST_CODE_NOTE_ACTIVITY = 333;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // click first item on NavigationDrawer
        navigationView.getMenu().getItem(0).setChecked(true);

        // replace first Fragment
        final int ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fram_container,new Frag_all_note())
                .commit();
        //close keyboard
        setupUI(findViewById(R.id.drawer_layout));

        check = true;

        Note_Name_From_User = new EditText(this);

//        ImageButton imgbutton = (ImageButton)findViewById(R.id.action_refresh);
//        imgbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //testing refresh button
//                if (check==true)
//                {
//                    Fragment newFragment = new Refresh_Layout();
//                    final int ft = getSupportFragmentManager().beginTransaction()
//                            //replace with loading fragment
//                            .replace(R.id.fram_container,new Refresh_Layout())
//                            .commit();
//                    check=false;
//                }else {
//                    Fragment newFragment = new Refresh_Layout();
//                    final int ft = getSupportFragmentManager().beginTransaction()
//                            //replace with Frag_all_note fragment
//                            .replace(R.id.fram_container,new Frag_all_note())
//                            .commit();
//                    navigationView.getMenu().getItem(0).setChecked(true);
//                    check = true;
//                }
//            }
//        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View input_note_name = layoutInflater.inflate(R.layout.add_new_note_layout,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppTheme_Blue_Dialog);
                alertDialogBuilder.setView(input_note_name);

                final EditText userInput = (EditText) input_note_name
                        .findViewById(R.id.txt_note_name);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Create",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        note_name.equals(userInput.getText());
                                        Note_Name_From_User.setText(userInput.getText());
                                        Intent noteIntent = new Intent(mContext,NoteActivity.class);
                                        noteIntent.putExtra("TITLE_KEY",userInput.getText().toString().trim());
                                        startActivityForResult(noteIntent, REQUEST_CODE_NOTE_ACTIVITY);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        sv= (android.support.v7.widget.SearchView) findViewById(R.id.action_search);
        //SEARCH
        sv.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                DatabaseHandler db = new DatabaseHandler(getBaseContext());
                final Recyler_View_Adapter adapter=new Recyler_View_Adapter(db.getAllNote(),getApplication());
                RecyclerView rv = (RecyclerView)findViewById(R.id.note_recycler_view);
                rv.setAdapter(adapter);

                adapter.getFilter().filter(query);

                Toast.makeText(mContext, "TextChange", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);

        // replace first Fragment
        final int ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fram_container,new Frag_all_note())
                .commit();
    }

    //hide keyboard when click != edit text && TextInputEditText
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box and TextInputEditText views to hide keyboard.
        if (!(view instanceof EditText) && !(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE_NOTE_ACTIVITY){

                String title = data.getStringExtra(NoteActivity.EXTRA_DATA);
                note_name = title;
                Fragment newFragment = new Frag_all_note();
                final int ft = getSupportFragmentManager().beginTransaction()
                        //replace with loading fragment
                        .replace(R.id.fram_container,new Frag_all_note())
                        .commit();
                Toast.makeText(mContext, "FROM NOTEACTIVITY " + title, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_refresh) {
//            Log.d("", "onOptionsItemSelected: Refresh");
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment  frag = null;
        
        switch (id ) {
            case R.id.nav_All_Note:
                frag = new Frag_all_note();
                FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                sv.setVisibility(View.VISIBLE);
                break;

            case R.id.nav_Account_Setting:
                frag = new Frag_account_setting();
                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.GONE);
                sv.setVisibility(View.GONE);
                break;

            case R.id.nav_Setting:
                frag = new Frag_setting();
                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.GONE);
                sv.setVisibility(View.GONE);
                break;

            case R.id.nav_About:
                frag = new Frag_about();
                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.GONE);
                sv.setVisibility(View.GONE);
                break;

            default:
                frag = new Frag_all_note();
                fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setVisibility(View.VISIBLE);
                sv.setVisibility(View.VISIBLE);
                break;
        }

        if (frag != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fram_container, frag).commit(); //.addToBackStack("Fragment") add addToBackStack so when we press backpress
                                                                  // it will back 1 by 1
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

