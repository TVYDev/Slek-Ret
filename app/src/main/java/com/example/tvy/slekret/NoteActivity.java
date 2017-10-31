package com.example.tvy.slekret;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.ByteArrayAdapter;
import com.koushikdutta.ion.Ion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, View.OnDragListener, View.OnLongClickListener{

    public static Context mContext;
    public static int noteID;
    private static final int REQUEST_CODE_IMAGE = 111;
    private static final int REQUEST_CODE_CAMERA = 222;
    private static final int REQUEST_CODE_PAINT = 444;
    public static final String EXTRA_DATA = "1212";
    private static final int REQUEST_CODE_WRITE_STORAGE = 555;
    boolean grandWriteStorage = false;
    static RelativeLayout layoutForNote;
    LinearLayout topBar,functionsBar,formatTextBar,colorBar,resizeBar;

    DatabaseHandler db;

    TextView tvTitle;
    Button btnCancel, btnSave;
    Button btnPaint, btnText, btnImage, btnCamera;
    Button btnCheck, btnBold, btnItalic, btnUnderline, btnStrike, btnIncrease, btnDecrease, btnAlignL, btnAlignC, btnAlighR;
    Button btnDone, btnSmaller, btnBigger, btnFitScreen;

    private static final String TAG = "NoteActivity";

    Matrix matrix = new Matrix();
    static float scale = 1f;
    static float lastScaleValue = 1f;
    ScaleGestureDetector scaleGestureDetector;
    ImageView currentResizeImg = null;
    int currentPosImgArr = 0;
    int currentPosBtnArr = 0;
    String currentCamImage = "";
    String currentPaintImage = "";
    ArrayList<ImageView> arrImg = new ArrayList<>();
    ArrayList<Button> arrBtn = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Uri imageUri = null;
            String path = "sdcard/camera_app/";
            String paintPath = "";
            int num = 0;
            switch(requestCode){
                case REQUEST_CODE_IMAGE:
                    imageUri = data.getData();
                    num = 1;
                    break;
                case REQUEST_CODE_CAMERA:
                    path = path + currentCamImage;
                    num = 2;
                    break;
                case REQUEST_CODE_PAINT:
                    Log.d(TAG, "onActivityResult: REQUEST_CODE_PAINT");
                    paintPath = data.getStringExtra("DaTa");
                    num = 3;
                    break;
                default:
                    Log.d(TAG, "onActivityResult: NO Request code matched");
                    break;
            }

            ImageView image = new ImageView(layoutForNote.getContext());

            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setId(new Random().nextInt(200000-100000 + 1) + 100000);
            image.bringToFront();
            image.setScaleType(ImageView.ScaleType.MATRIX);
            image.setAdjustViewBounds(true);
            //scaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener(image));

            if(num == 1) {
                Glide.with(this)
                        .load(imageUri)
                        .into(image);
            }else if(num == 2){
                Glide.with(this)
                        .load(path)
                        .into(image);
            }else if(num == 3){

                Glide.with(this)
                        .load(paintPath)
                        .into(image);
            }

            layoutForNote.addView(image);
            image.setOnClickListener(this);
            image.setOnLongClickListener(this);
            image.setClickable(true);
            image.setLongClickable(true);

            arrImg.add(image);
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: x=" + v.getX() + "  y= " + v.getY());
        final View view = v;

        for(int i=0;i<arrBtn.size();i++){
            if(arrBtn.get(i).getId() == view.getId()){
                currentPosBtnArr = i;
                PopupMenu popupMenu = new PopupMenu(layoutForNote.getContext(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.popup_items_text, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.id_format) {
                            setModeFormatText();
                            btnSave.setVisibility(View.INVISIBLE);
                            btnCancel.setVisibility(View.INVISIBLE);
                            Button b = (Button)view;
                            if(b.getTypeface().getStyle()==Typeface.BOLD){
                                btnBold.setBackgroundResource(R.mipmap.ic_format_bold_white_18dp);
                            }else{
                                btnBold.setBackgroundResource(R.mipmap.ic_format_bold_black_18dp);
                            }
                            if(b.getTypeface().getStyle()==Typeface.ITALIC){
                                btnItalic.setBackgroundResource(R.mipmap.ic_format_italic_white_18dp);
                            }else{
                                btnItalic.setBackgroundResource(R.mipmap.ic_format_italic_black_18dp);
                            }
                            if(b.getPaintFlags()==Paint.UNDERLINE_TEXT_FLAG){
                                btnUnderline.setBackgroundResource(R.mipmap.ic_format_underlined_white_18dp);
                            }else{
                                btnUnderline.setBackgroundResource(R.mipmap.ic_format_underlined_black_18dp);
                            }
                            if(b.getPaintFlags()==Paint.STRIKE_THRU_TEXT_FLAG){
                                btnStrike.setBackgroundResource(R.mipmap.ic_format_strikethrough_white_18dp);
                            }else{
                                btnStrike.setBackgroundResource(R.mipmap.ic_format_strikethrough_black_18dp);
                            }
                            if(b.getTextAlignment()==View.TEXT_ALIGNMENT_TEXT_START){
                                btnAlignL.setBackgroundResource(R.mipmap.ic_format_align_left_white_18dp);
                            }else{
                                btnAlignL.setBackgroundResource(R.mipmap.ic_format_align_left_black_18dp);
                            }
                            if(b.getTextAlignment()==View.TEXT_ALIGNMENT_TEXT_END){
                                btnAlighR.setBackgroundResource(R.mipmap.ic_format_align_right_white_18dp);
                            }else{
                                btnAlighR.setBackgroundResource(R.mipmap.ic_format_align_right_black_18dp);
                            }
                            if(b.getTextAlignment()==View.TEXT_ALIGNMENT_CENTER){
                                btnAlignC.setBackgroundResource(R.mipmap.ic_format_align_center_white_18dp);
                            }else{
                                btnAlignC.setBackgroundResource(R.mipmap.ic_format_align_center_black_18dp);
                            }
                            Log.d("", "onMenuItemClick: format");
                        }else if(item.getItemId() == R.id.id_edit){
                            LayoutInflater layoutInflater = LayoutInflater.from(layoutForNote.getContext());
                            View vv = layoutInflater.inflate(R.layout.input_text,null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(layoutForNote.getContext());
                            builder.setView(vv);
                            final EditText edText = (EditText)vv.findViewById(R.id.edtText);
                            edText.setText(arrBtn.get(currentPosBtnArr).getText().toString().trim());
                            builder
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(edText.getText().toString().trim().equals("")){
                                                Toast.makeText(NoteActivity.this, "Text is not created.\nYou need to enter text.", Toast.LENGTH_SHORT).show();
                                            }else {
                                                arrBtn.get(currentPosBtnArr).setText(edText.getText().toString().trim());
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else if(item.getItemId() == R.id.id_delete){
                            deleteView(view);
                            Log.d("", "onMenuItemClick: delete");
                        }else if(item.getItemId() == R.id.id_bringToFront){
                            bringToFront(view);
                            Log.d("", "onMenuItemClick: bring to front");
                        }else if(item.getItemId() == R.id.id_sendToBack){
                            sendViewToBack(view);
                            Log.d("", "onMenuItemClick: send to back");
                        }else{}
                        return true;
                    }
                });
                popupMenu.show();
                return;
            }
        }


        for(int i=0;i<arrImg.size();i++){
            if(arrImg.get(i).getId() == view.getId()){
                currentPosImgArr = i;
                Log.d(TAG, "onClick: currentPosImgArr = "+i + " size = " + arrImg.size() + " id =" + view.getId());
                PopupMenu popupMenu = new PopupMenu(layoutForNote.getContext(), v);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.popup_items_image, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.id_resize) {
//                            arrImg.get(currentPosImgArr).setOnClickListener(null);
//                            arrImg.get(currentPosImgArr).setOnLongClickListener(null);
//                            scaleGestureDetector = null;
//                            scaleGestureDetector = new ScaleGestureDetector(layoutForNote.getContext(),new ScaleListener(arrImg.get(currentPosImgArr)));
//                            for(int i=0;i<arrImg.size();i++){
//                                arrImg.get(i).setClickable(false);
//                                arrImg.get(i).setLongClickable(false);
//                            }
//                            scaleImage(arrImg.get(currentPosImgArr));
                            btnSave.setVisibility(View.INVISIBLE);
                            btnCancel.setVisibility(View.INVISIBLE);
                            setModeResize();
                            Log.d("", "onMenuItemClick: resize");
                        }else if(item.getItemId() == R.id.id_delete){
                            deleteView(view);
                            Log.d("", "onMenuItemClick: delete");
                        }else if(item.getItemId() == R.id.id_bringToFront){
                            bringToFront(view);
                            Log.d("", "onMenuItemClick: bring to front");
                        }else if(item.getItemId() == R.id.id_sendToBack){
                            sendViewToBack(view);
                            Log.d("", "onMenuItemClick: send to back");
                        }else{}
                        return true;
                    }
                });
                popupMenu.show();
                return;
            }
        }

        switch (v.getId()){
            case R.id.tvTitle:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View vv = layoutInflater.inflate(R.layout.rename_note,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(vv);
                final EditText edText = (EditText)vv.findViewById(R.id.edtText);
                edText.setText(tvTitle.getText());
                builder
                        .setCancelable(true)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(edText.getText().toString().trim().equals("")){
                                    Toast.makeText(NoteActivity.this, "Your note is NOT renamed", Toast.LENGTH_SHORT).show();
                                }else {
                                    tvTitle.setText(edText.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Log.d(TAG, "onClick: tvTitle");
                break;
            case R.id.btnSave:
                boolean bool = false;
                List<Integer> listNoteID = db.getAllNoteId();
                for(int i=0;i<listNoteID.size();i++){
                    if(listNoteID.get(i) == noteID){
                        bool = true;
                        break;
                    }
                }

                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy     hh:mm aa");
                String formattedDate = df.format(c.getTime());

                //b == false means Save new
                //b == true means Save edit
                if(bool == false){
                    noteID = new Random().nextInt(900000-500000 + 1) + 500000;
                    Log.d(TAG, "onClick: noteID = " + noteID);
                    TableNote tbNote = new TableNote();
                    tbNote.setId(noteID);
                    tbNote.setTitle(tvTitle.getText().toString());
                    tbNote.setDate(formattedDate);
                    db.addNote(tbNote);
                    Log.d(TAG, "onClick: btnSave save new");
                }else{
                    db.updateNote(noteID,tvTitle.getText().toString(),formattedDate);
                    db.deleteButtonsOfNote(noteID);
                    db.deleteImagesOfNote(noteID);
                    Log.d(TAG, "onClick: btnSave save edit");
                }

                List<View> lstView = new ArrayList<>();
                for(int i=0;i<layoutForNote.getChildCount();i++){
                    lstView.add(layoutForNote.getChildAt(i));
                }
                Log.d(TAG, "onClick: layoutForNote childcount = " + layoutForNote.getChildCount());
                int viewID = 0;
                Bitmap bitmap = null;
                ImageView img;
                TableImage tbImg = null;

//                ArrayList<byte[]> arrByte = new ArrayList<>();

                for(int i=0;i<lstView.size();i++){
                    viewID = lstView.get(i).getId();
                    Log.d(TAG, "onClick: viewID[" + i + "] = " + viewID);
                    if(viewID>100000 && viewID<200000){
                        img = (ImageView)lstView.get(i);
                        tbImg = new TableImage();
                        tbImg.setId(img.getId());
                        tbImg.setImgX((int)img.getX());
                        tbImg.setImgY((int)img.getY());
                        tbImg.setWidth(img.getWidth());
                        tbImg.setHeight(img.getHeight());

                        Toast.makeText(mContext, img.getId() + "", Toast.LENGTH_SHORT).show();

                        bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
//                        arrByte.add(getBitmapAsByteArray(bitmap));
                        img = null;
                        tbImg.setSrc(getBitmapAsByteArray(bitmap));
                        bitmap = null;
                        tbImg.setNoteId(noteID);
                        db.addImage(tbImg);
                        tbImg = null;

                        Log.d(TAG, "onClick: image is added");
                    }else if(viewID>200000 && viewID<300000){
                        Button btn = (Button)lstView.get(i);
                        TableButton tbBtn = new TableButton();
                        tbBtn.setId(btn.getId());
                        tbBtn.setX((int)btn.getX());
                        tbBtn.setY((int)btn.getY());
                        tbBtn.setText(btn.getText().toString());
                        tbBtn.setTxtSize((int)btn.getTextSize());
                        tbBtn.setTxtAlign(btn.getTextAlignment());
                        tbBtn.setTxtTypeface(btn.getTypeface().getStyle());
                        tbBtn.setTxtFlag(btn.getPaintFlags());
                        tbBtn.setTxtColor(btn.getCurrentTextColor());
                        tbBtn.setNoteID(noteID);
                        db.addButton(tbBtn);
                        Log.d(TAG, "onClick: button is added");
                    }else{}
                }

                Log.d(TAG, "onClick: note ID " + noteID);
                Log.d(TAG, "onClick: Note count" + db.getNoteCount());
                Log.d(TAG, "onClick: Button Count" + db.getButtonCount(noteID));
                Log.d(TAG, "onClick: Image count" + db.getImageCount(noteID));

//                for(int i=0;i<arrByte.size();i++){
//                    for(int j=0;j<arrByte.size();i++){
//                        if(arrByte.get(i).equals(arrByte.get(j))){
//                            Log.d(TAG, "onClick: arrByte[" + i + "] is equal to arrByte[" + j + "]");
//                        }else{
//                            Log.d(TAG, "onClick: arrByte[" + i + "] is NOT equal to arrByte[" + j + "]");
//                        }
//                    }
//                }

                Intent data = new Intent();
                data.putExtra(EXTRA_DATA, tvTitle.getText().toString());
                setResult(Activity.RESULT_OK, data);
                noteID = 0;
                finish();
                Log.d(TAG, "onClick: btnSave completed");
                break;
            case R.id.btnCancel:
                finish();
                Log.d(TAG, "onClick: btnCancel");
                break;
            case R.id.btnPaint:

                final Intent intent = new Intent(this, PaintActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PAINT);
                Log.d(TAG, "onClick: btnPaint");
                break;
            case R.id.btnText:
                LayoutInflater layoutInflater1 = LayoutInflater.from(this);
                View vv1 = layoutInflater1.inflate(R.layout.input_text,null);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(vv1);
                final EditText edText1 = (EditText)vv1.findViewById(R.id.edtText);
                builder1
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(edText1.getText().toString().trim().equals("")){
                                    Toast.makeText(NoteActivity.this, "Text is not created.\nYou need to enter text.", Toast.LENGTH_SHORT).show();
                                }else {
                                    insertText(edText1.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                Log.d(TAG, "onClick: btnText");
                break;
            case R.id.btnImage:
                insertImage();
                Log.d(TAG, "onClick: btnImage");
                break;
            case R.id.btnCamera:
                grandWriteStorage = false;
                askPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_STORAGE);
                if(grandWriteStorage == true) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getFile();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                }
                Log.d(TAG, "onClick: btnCamera");
                break;
            case R.id.btnSmaller:
                int bound = arrImg.get(currentPosImgArr).getWidth();
                scaleImage(arrImg.get(currentPosImgArr), bound-20);
//                scale = 0.9f;
//                arrImg.get(currentPosImgArr).setScaleType(ImageView.ScaleType.CENTER);
//                matrix.setScale(scale,scale);
//                arrImg.get(currentPosImgArr).setImageMatrix(matrix);
//                arrImg.get(currentPosImgArr).getLayoutParams().height -= 10;
//                arrImg.get(currentPosImgArr).getLayoutParams().width -= 10;
//                arrImg.get(currentPosImgArr).requestLayout();
//                arrImg.get(currentPosImgArr).setAdjustViewBounds(true);
                Log.d(TAG, "onClick: btnSmaller");
                break;
            case R.id.btnBigger:
                int bound1 = arrImg.get(currentPosImgArr).getWidth();
                scaleImage(arrImg.get(currentPosImgArr), bound1+20);
//                arrImg.get(currentPosImgArr).getLayoutParams().height += 10;
//                arrImg.get(currentPosImgArr).getLayoutParams().width += 10;
//                arrImg.get(currentPosImgArr).requestLayout();
                Log.d(TAG, "onClick: btnBigger");
                break;
            case R.id.btnFitScreen:
              ViewGroup vg = (ViewGroup)arrImg.get(currentPosImgArr).getParent();
                int bound2 = vg.getWidth();
                scaleImage(arrImg.get(currentPosImgArr), bound2);
                Log.d(TAG, "onClick: btnFitScreen");
                break;
            case R.id.btnDone:
                setModeFunction();
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: btnDone");
                break;
//            case R.id.btnReset:
//                //!!!!!!!!!!!Not yet Done
//                matrix.setScale(lastScaleValue, lastScaleValue);
//                arrImg.get(currentPosImgArr).setImageMatrix(matrix);
//                arrImg.get(currentPosImgArr).requestLayout();
//                ViewGroup.LayoutParams layoutParams = arrImg.get(currentPosImgArr).getLayoutParams();
//                layoutParams.width =(int)(arrImg.get(currentPosImgArr).getWidth()*lastScaleValue);
//                layoutParams.height =(int)(arrImg.get(currentPosImgArr).getHeight()*lastScaleValue);
//                arrImg.get(currentPosImgArr).setLayoutParams(layoutParams);
//                Log.d(TAG, "onClick: btnReset");
//                break;
//            case R.id.btnOkay:
//                scaleGestureDetector = null;
//                for(int i=0;i<arrImg.size();i++){
//                    arrImg.get(i).setClickable(true);
//                    arrImg.get(i).setLongClickable(true);
//                }
//                btnCancel.setVisibility(View.VISIBLE);
//                btnSave.setVisibility(View.VISIBLE);
//                setModeFunction();
//                Log.d(TAG, "onClick: btnOkay");
//                break;
            case R.id.btnCheckFormatText:
                setModeFunction();
                btnSave.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: btnCheckFormatText");
                break;
            case R.id.btnBold:
                Button b = arrBtn.get(currentPosBtnArr);
                if(b.getTypeface().getStyle()== Typeface.BOLD) {
                    b.setTypeface(Typeface.DEFAULT);
                    v.setBackgroundResource(R.mipmap.ic_format_bold_black_18dp);
                }else if (b.getTypeface().getStyle() == Typeface.BOLD_ITALIC){
                    b.setTypeface(Typeface.DEFAULT);
                    b.setTypeface(b.getTypeface(), Typeface.ITALIC);
                    v.setBackgroundResource(R.mipmap.ic_format_bold_black_18dp);
                }else {
                    if(b.getTypeface().getStyle() == Typeface.ITALIC){
                        b.setTypeface(b.getTypeface(), Typeface.BOLD_ITALIC);
                    }else {
                        b.setTypeface(b.getTypeface(), Typeface.BOLD);
                    }
                    v.setBackgroundResource(R.mipmap.ic_format_bold_white_18dp);
                }
                Log.d(TAG, "onClick: btnBold");
                break;
            case R.id.btnItalic:
                Button b1 = arrBtn.get(currentPosBtnArr);
                if(b1.getTypeface().getStyle() == Typeface.ITALIC){
                    b1.setTypeface(Typeface.DEFAULT);
                    v.setBackgroundResource(R.mipmap.ic_format_italic_black_18dp);
                }else if(b1.getTypeface().getStyle() == Typeface.BOLD_ITALIC){
                    b1.setTypeface(Typeface.DEFAULT);
                    b1.setTypeface(b1.getTypeface(), Typeface.BOLD);
                    v.setBackgroundResource(R.mipmap.ic_format_italic_black_18dp);
                }else{
                    if(b1.getTypeface().getStyle() == Typeface.BOLD){
                        b1.setTypeface(b1.getTypeface(), Typeface.BOLD_ITALIC);
                    }else{
                        b1.setTypeface(b1.getTypeface(), Typeface.ITALIC);
                    }
                    v.setBackgroundResource(R.mipmap.ic_format_italic_white_18dp);
                }
                Log.d(TAG, "onClick: btnItalic");
                break;
            case R.id.btnUnderline:
                Button b3 = arrBtn.get(currentPosBtnArr);
                if(b3.getPaintFlags() == Paint.UNDERLINE_TEXT_FLAG){
                    Log.d(TAG, "onClick: 111111");
                    b3.setPaintFlags(0);
                    v.setBackgroundResource(R.mipmap.ic_format_underlined_black_18dp);
                }else if(b3.getPaintFlags() == (Paint.UNDERLINE_TEXT_FLAG | Paint.STRIKE_THRU_TEXT_FLAG)){
                    Log.d(TAG, "onClick: 222222");
                    b3.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    v.setBackgroundResource(R.mipmap.ic_format_underlined_black_18dp);
                }else{
                    Log.d(TAG, "onClick: 333333");
                    b3.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG | b3.getPaintFlags());
                    v.setBackgroundResource(R.mipmap.ic_format_underlined_white_18dp);
                }
                Log.d(TAG, "onClick: btnUnderline");
                break;
            case R.id.btnStrike:
                Button b4 = arrBtn.get(currentPosBtnArr);
                if(b4.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG){
                    b4.setPaintFlags(0);
                    v.setBackgroundResource(R.mipmap.ic_format_strikethrough_black_18dp);
                }else if(b4.getPaintFlags() == (Paint.UNDERLINE_TEXT_FLAG | Paint.STRIKE_THRU_TEXT_FLAG)){
                    b4.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    v.setBackgroundResource(R.mipmap.ic_format_strikethrough_black_18dp);
                }else{
                    b4.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | b4.getPaintFlags());
                    v.setBackgroundResource(R.mipmap.ic_format_strikethrough_white_18dp);
                }
                Log.d(TAG, "onClick: btnStrikeThrough");
                break;
            case R.id.btnAlignLeft:
                Button b5 = arrBtn.get(currentPosBtnArr);
                if(b5.getTextAlignment() != View.TEXT_ALIGNMENT_TEXT_START){
                    b5.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    btnAlignL.setBackgroundResource(R.mipmap.ic_format_align_left_white_18dp);
                    btnAlignC.setBackgroundResource(R.mipmap.ic_format_align_center_black_18dp);
                    btnAlighR.setBackgroundResource(R.mipmap.ic_format_align_right_black_18dp);
                }
                Log.d(TAG, "onClick: btnAlignLeft");
                break;
            case R.id.btnAlignCenter:
                Button b6 = arrBtn.get(currentPosBtnArr);
                if(b6.getTextAlignment() != View.TEXT_ALIGNMENT_CENTER){
                    b6.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    btnAlignL.setBackgroundResource(R.mipmap.ic_format_align_left_black_18dp);
                    btnAlignC.setBackgroundResource(R.mipmap.ic_format_align_center_white_18dp);
                    btnAlighR.setBackgroundResource(R.mipmap.ic_format_align_right_black_18dp);
                }
                Log.d(TAG, "onClick: btnAlignCenter");
                break;
            case R.id.btnAlignRight:
                Button b7 = arrBtn.get(currentPosBtnArr);
                if(b7.getTextAlignment() != View.TEXT_ALIGNMENT_TEXT_END){
                    b7.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    btnAlignL.setBackgroundResource(R.mipmap.ic_format_align_left_black_18dp);
                    btnAlignC.setBackgroundResource(R.mipmap.ic_format_align_center_black_18dp);
                    btnAlighR.setBackgroundResource(R.mipmap.ic_format_align_right_white_18dp);
                }
                Log.d(TAG, "onClick: btnAlignLeft");
                break;
            case R.id.btnIncreaseTextSize:
                Button b8 = arrBtn.get(currentPosBtnArr);
                if(b8.getTextSize()<680) {
                    b8.setTextSize(TypedValue.COMPLEX_UNIT_PX, b8.getTextSize() + 10);
                }
                Log.d(TAG, "onClick: btnIncreaseTextSize  " + b8.getTextSize());
                break;
            case R.id.btnDecreaseTextSize:
                Button b9 = arrBtn.get(currentPosBtnArr);
                if(b9.getTextSize()>10) {
                    b9.setTextSize(TypedValue.COMPLEX_UNIT_PX, b9.getTextSize() - 10);
                }
                Log.d(TAG, "onClick: btnIncreaseTextSize  " + b9.getTextSize());
                break;
            default:
                break;
        }
    }

    private void askPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
            //we do not have permission
            ActivityCompat.requestPermissions(this, new String[]{permission},requestCode);
        }else{
            //we have permission granted
            grandWriteStorage=true;
            Log.d(TAG, "askPermission: Permission is already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE_WRITE_STORAGE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    grandWriteStorage = false;
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getFile();
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                    Log.d(TAG, "onRequestPermissionsResult: permission is granted");
                }else{
                    grandWriteStorage = true;
                }
                break;
            default:
                Log.d(TAG, "onRequestPermissionsResult: No request code permission matched");
                break;
        }
    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");
        if(!folder.exists()){
            folder.mkdir();
        }
        int r = new Random().nextInt(90000-10000 + 1) + 10000;
        currentCamImage =  "cam_image" + r + ".jpg";
        File imageFile = new File(folder, currentCamImage);

        return imageFile;
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if(scaleGestureDetector != null) {
//            scaleGestureDetector.onTouchEvent(event);
//            ImageView img = arrImg.get(currentPosImgArr);
//            img.requestLayout();
//            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
//            layoutParams.width =(int)(img.getWidth()*scale);
//            layoutParams.height =(int)(img.getHeight()*scale);
//            img.setLayoutParams(layoutParams);
//            lastScaleValue = scale;
//            return true;
//        }
//        return false;
//    }

//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
//        ImageView imageView;
//
//        public ScaleListener(ImageView imageView){
//            this.imageView = imageView;
//        }
//
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            scale = scale * detector.getScaleFactor();
//            scale = Math.max(0.1f, Math.min(scale, 5f));
//            matrix.setScale(scale,scale);
//            imageView.setImageMatrix(matrix);
//            return true;
//        }
//    }

    private void insertTextFromDB(int noteID, DatabaseHandler db){
        List<TableButton> btns = db.getButtons(noteID);
        Log.d(TAG, "insertTextFromDB: " + db.getButtonCount(noteID));
        arrBtn.clear();
        for(int i=0;i<btns.size();i++) {
            Button btn = new Button(mContext);
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setAllCaps(false);
            btn.setId(new Random().nextInt(300000-200000 + 1) + 200000);
            btn.setX(btns.get(i).getX());
            btn.setY(btns.get(i).getY());
            btn.setText(btns.get(i).getText().toString());
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btns.get(i).getTxtSize());
            if(btns.get(i).getTxtAlign() == 2){
                btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            }else if(btns.get(i).getTxtAlign() == 4){
                btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }else if(btns.get(i).getTxtAlign() == 3){
                btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            }else{}
            btn.setTextColor(btns.get(i).getTxtColor());
            btn.setTypeface(Typeface.DEFAULT);
            btn.setTypeface(btn.getTypeface(), btns.get(i).getTxtTypeface());
            btn.setPaintFlags(btns.get(i).getTxtFlag());
            arrBtn.add(btn);
            btn.setOnClickListener(this);
            btn.setOnLongClickListener(this);
            layoutForNote.addView(btn);
        }
    }

    private void insertImageFromDB(int noteId, DatabaseHandler db){
        List<TableImage> images = db.getImages(noteId);
        Log.d(TAG, "insertImageFromDB: count" + db.getImageCount(noteId));
        arrImg.clear();
        for(int i=0;i<images.size();i++){
            ImageView image = new ImageView(layoutForNote.getContext());

            image.setLayoutParams(new ViewGroup.LayoutParams(images.get(i).getWidth(), images.get(i).getHeight()));
            image.setX(images.get(i).getImgX());
            image.setY(images.get(i).getImgY());
//            int id = new Random().nextInt(200000-100000 + 1) + 100000;
            image.setId(new Random().nextInt(200000-100000 + 1) + 100000);
//            image.setId(images.get(i).getId());

//            Bitmap bmp = BitmapFactory.decodeByteArray(imgs.get(i).getSrc(), 0, imgs.get(i).getSrc().length);

//            Toast.makeText(mContext, images.get(i).getId(), Toast.LENGTH_SHORT).show();
            Glide.with(this)
                    .load(images.get(i).getSrc())
                    .into(image);

            layoutForNote.addView(image);
            image.setOnClickListener(this);
            image.setOnLongClickListener(this);

            arrImg.add(image);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mContext = getBaseContext();
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE_KEY");
//        Intent intentFromMain = getIntent();
        int iid = intent.getIntExtra("ID_KEY",0);

        layoutForNote = (RelativeLayout) findViewById(R.id.layoutForNote);
//        layoutForNote.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        ViewGroup.LayoutParams layoutParams= layoutForNote.getLayoutParams();
//        layoutParams.height = 1000;
//        layoutForNote.setLayoutParams(layoutParams);




        layoutForNote.setOnDragListener(this);
        topBar = (LinearLayout)findViewById(R.id.topbar);
        functionsBar = (LinearLayout)findViewById(R.id.functionsBar);
        formatTextBar = (LinearLayout)findViewById(R.id.formatTextBar);
        colorBar = (LinearLayout)findViewById(R.id.colorPickerBar);
        resizeBar = (LinearLayout)findViewById(R.id.resizeBar);
        setModeFunction();

        btnSave = (Button)findViewById(R.id.btnSave);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvTitle.setOnClickListener(this);


        Log.d(TAG, "onCreate: " + title);
        tvTitle.setText(title);

        btnPaint = (Button)findViewById(R.id.btnPaint);
        btnText = (Button)findViewById(R.id.btnText);
        btnImage = (Button)findViewById(R.id.btnImage);
        btnCamera = (Button)findViewById(R.id.btnCamera);
        btnPaint.setOnClickListener(this);
        btnText.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        btnCheck = (Button)findViewById(R.id.btnCheckFormatText);
        btnBold = (Button)findViewById(R.id.btnBold);
        btnItalic = (Button)findViewById(R.id.btnItalic);
        btnUnderline = (Button)findViewById(R.id.btnUnderline);
        btnStrike = (Button)findViewById(R.id.btnStrike);
        btnIncrease = (Button)findViewById(R.id.btnIncreaseTextSize);
        btnDecrease = (Button)findViewById(R.id.btnDecreaseTextSize);
        btnAlignL = (Button)findViewById(R.id.btnAlignLeft);
        btnAlignC = (Button)findViewById(R.id.btnAlignCenter);
        btnAlighR = (Button)findViewById(R.id.btnAlignRight);
        btnCheck.setOnClickListener(this);
        btnBold.setOnClickListener(this);
        btnItalic.setOnClickListener(this);
        btnUnderline.setOnClickListener(this);
        btnStrike.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        btnDecrease.setOnClickListener(this);
        btnAlignL.setOnClickListener(this);
        btnAlignC.setOnClickListener(this);
        btnAlighR.setOnClickListener(this);

        btnDone = (Button)findViewById(R.id.btnDone);
        btnSmaller = (Button)findViewById(R.id.btnSmaller);
        btnBigger = (Button)findViewById(R.id.btnBigger);
        btnFitScreen = (Button)findViewById(R.id.btnFitScreen);
        btnDone.setOnClickListener(this);
        btnSmaller.setOnClickListener(this);
        btnBigger.setOnClickListener(this);
        btnFitScreen.setOnClickListener(this);




        db = new DatabaseHandler(getApplicationContext());
//        db.deleteTableButton();
//        db.deleteTableNote();
        Log.d(TAG, "onCreate: note count =" + db.getNoteCount());
        Log.d(TAG, "onCreate: all button count=" + db.getAllButtonCount());
        Log.d(TAG, "onCreate: all image count=" + db.getAllImageCount());

        if(iid!=0) {

            noteID = iid;
            insertTextFromDB(noteID, db);
            insertImageFromDB(noteID, db);
            Log.d(TAG, "onCreate: button count = " + db.getButtonCount(noteID));
            Log.d(TAG, "onCreate: image count = " + db.getImageCount(noteID));

            ViewGroup vg1 = layoutForNote;
            float maxY1 = 0;
            for (int i = 0; i < vg1.getChildCount(); i++) {
                if ((vg1.getChildAt(i).getY() + vg1.getChildAt(i).getHeight()) > maxY1) {
                    maxY1 = vg1.getChildAt(i).getY() + vg1.getChildAt(i).getHeight() / 2;
                }
            }
            ViewGroup.LayoutParams lp1 = layoutForNote.getLayoutParams();
            lp1.height = (int) maxY1 + 1500;
            layoutForNote.setLayoutParams(lp1);
            layoutForNote.invalidate();

//        db.deleteButtonsOfNote(886012);
//        insertTextFromDB(886012,db);

//            List<Integer> lst = db.getAllNoteId();
//            for (int i = 0; i < lst.size(); i++) {
////                Log.d(TAG, "onCreate: Note ID" + lst.get(i));
//                Toast.makeText(mContext, "onCreate: Note ID" + lst.get(i), Toast.LENGTH_LONG).show();
//            }

            List<Integer> lst = db.getAllImagesId();
            for(int i=0;i<lst.size();i++){
                Toast.makeText(mContext, "onCreate: ImageID " + lst.get(i), Toast.LENGTH_SHORT).show();
            }
        }
//        Log.d(TAG, "onCreate: Note count " + db.getNoteCount());
    }

    public void setModeFunction(){
        functionsBar.setVisibility(View.VISIBLE);
        formatTextBar.setVisibility(View.GONE);
        colorBar.setVisibility(View.GONE);
        resizeBar.setVisibility(View.GONE);
    }

    public void setModeFormatText(){
        functionsBar.setVisibility(View.GONE);
        formatTextBar.setVisibility(View.VISIBLE);
        colorBar.setVisibility(View.GONE);
        resizeBar.setVisibility(View.GONE);
    }

    public void setModeColor(){
        functionsBar.setVisibility(View.GONE);
        formatTextBar.setVisibility(View.GONE);
        colorBar.setVisibility(View.VISIBLE);
        resizeBar.setVisibility(View.GONE);
    }

    public void setModeResize(){
        functionsBar.setVisibility(View.GONE);
        formatTextBar.setVisibility(View.GONE);
        colorBar.setVisibility(View.GONE);
        resizeBar.setVisibility(View.VISIBLE);
    }

    public void insertImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE_IMAGE);
    }

    public void insertText(String text){
        Button btn = new Button(mContext);
        btn.setText(text);
        btn.setTextColor(Color.BLACK);
        btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setX(layoutForNote.getWidth()/2 - btn.getWidth()/2);
        btn.setY(layoutForNote.getHeight()/2 - btn.getHeight()/2);
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setId(new Random().nextInt(300000-200000 + 1) + 200000);
        btn.setAllCaps(false);
        btn.setTypeface(Typeface.DEFAULT);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100);
        btn.setPaintFlags(0);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        arrBtn.add(btn);
        //btn.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        btn.setOnClickListener(this);
        btn.setOnLongClickListener(this);
        //GradientDrawable gd = new GradientDrawable();
        //gd.setColor(Color.TRANSPARENT); // Changes this drawbale to use a single color instead of a gradient
        //gd.setCornerRadius(5);
        //gd.setStroke(1, 0xFF000000);
        //btn.setBackground(gd);
        layoutForNote.addView(btn);
    }

    public void bringToFront(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        Log.d(TAG, "bringToFront: " + parent.getChildCount());
        parent.requestLayout();
        if (parent != null) {
            parent.removeView(child);
            parent.addView(child);
        }
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        Log.d(TAG, "sendViewToBack: " + parent.getChildCount());
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public void deleteView(final View child){
        final ViewGroup parent = (ViewGroup)child.getParent();
        if(null != parent){
            parent.removeView(child);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View draggedView = (View)event.getLocalState();
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
//                ViewGroup.LayoutParams layoutParams = layoutForNote.getLayoutParams();
//                layoutParams.height = (int)event.getY() + draggedView.getBottom() ;
                ViewGroup vg = (ViewGroup)v.getParent();
                float maxY = 0;

                for(int i=0;i<vg.getChildCount();i++){
                    if((vg.getChildAt(i).getY() + vg.getChildAt(i).getHeight()/2) > maxY){
                        maxY = vg.getChildAt(i).getY() + vg.getChildAt(i).getHeight();
                    }
                }
                ViewGroup.LayoutParams lp = layoutForNote.getLayoutParams();
                lp.height = (int)maxY + 500;
                layoutForNote.setLayoutParams(lp);
                Log.d(TAG, "onDrag: maxY=" + maxY + "  height=" + lp.height);
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                ViewGroup.LayoutParams layoutParams= layoutForNote.getLayoutParams();
                //Log.d(TAG, "NNNNNN: " + layoutParams.height + "   " + event.getY() + "  " + draggedView.getHeight());
                if((layoutParams.height - (event.getY()) + draggedView.getHeight()/2) >= 30){
                    //Log.d(TAG, "NNNNNN: " + layoutParams.height + "   " + event.getY() + "  " + draggedView.getBottom());
                    layoutParams.height += 100;
                    layoutForNote.setLayoutParams(layoutParams);
                }
                Log.d("LOCATION", "x = " + event.getX() + "  y = " + event.getY());



                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DROP:
                draggedView.setVisibility(View.VISIBLE);
                draggedView.setX(event.getX() - draggedView.getWidth()/2);
                draggedView.setY(event.getY() - draggedView.getHeight()/2);

                Log.d("DROP", "x = " + (event.getX() - draggedView.getWidth()/2) + "  y = " + (event.getY() - draggedView.getHeight()/2));



                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                ViewGroup vg1 = (ViewGroup)v.getParent();
                float maxY1 = 0;
                for(int i=0;i<vg1.getChildCount();i++){
                    if((vg1.getChildAt(i).getY() + vg1.getChildAt(i).getHeight()) > maxY1){
                        maxY1 = vg1.getChildAt(i).getY() + vg1.getChildAt(i).getHeight()/2;
                    }
                }
                ViewGroup.LayoutParams lp1 = layoutForNote.getLayoutParams();
                lp1.height = (int)maxY1 + 200;
                layoutForNote.setLayoutParams(lp1);
                layoutForNote.invalidate();
                Log.d(TAG, "onDrag: DROP maxY=" + maxY1 + "  height=" + lp1.height);
                Log.d(TAG, "onDrag: ended");
                return true;
            default:
                Log.d("Note Activity", "Unknown action type received by OnDragListener.");
        }
        return true;
    }


    private void scaleImage(ImageView view, int boudingSize) throws NoSuchElementException  {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
            bitmap = Ion.with(view).getBitmap();
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
//        int bounding = dpToPx(250);
        int bounding = boudingSize;
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData clipData = ClipData.newPlainText("","");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(clipData,shadowBuilder,v,0);
        v.setVisibility(View.INVISIBLE);
        return true;
    }
}
