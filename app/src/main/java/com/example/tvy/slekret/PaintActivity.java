package com.example.tvy.slekret;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class PaintActivity extends AppCompatActivity implements View.OnClickListener{

    DrawingView drawingView;
    private ImageButton currPaint, brush, eraser, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        drawingView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        eraser = (ImageButton)findViewById(R.id.btnEraser);
        eraser.setOnClickListener(this);
        brush = (ImageButton)findViewById(R.id.btnBrush);
        brush.setOnClickListener(this);
        save = (ImageButton)findViewById(R.id.btnSavePaint);
        save.setOnClickListener(this);
    }



    public void paintClicked(View view){
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawingView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] b = outputStream.toByteArray();
        outputStream.close();
        return b;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnEraser){
            drawingView.setErase(true);
        }else if(v.getId() == R.id.btnBrush){
            drawingView.setErase(false);
        }else if(v.getId() == R.id.btnSavePaint){
            drawingView.setDrawingCacheEnabled(true);

            String imgSaved = MediaStore.Images.Media.insertImage(
                    getContentResolver(), drawingView.getDrawingCache(),
                    UUID.randomUUID().toString() +".png", "drawing");

            String[] projection = new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.MIME_TYPE
            };
            final Cursor cursor = getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            cursor.moveToLast();
            String imageLocation = cursor.getString(1);

            final Intent data = new Intent();
            data.putExtra("DaTa", imageLocation);
            setResult(Activity.RESULT_OK, data);

            drawingView.destroyDrawingCache();

            finish();

//            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
//            saveDialog.setTitle("Save drawing");
//            saveDialog.setMessage("Save drawing to device Gallery?");
//            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//
//
////                    String imgSaved = MediaStore.Images.Media.insertImage(
////                                getContentResolver(), drawingView.getDrawingCache(),
////                            UUID.randomUUID().toString() +".png", "drawing");
////
////                    String[] projection = new String[]{
////                            MediaStore.Images.ImageColumns._ID,
////                            MediaStore.Images.ImageColumns.DATA,
////                            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
////                            MediaStore.Images.ImageColumns.DATE_TAKEN,
////                            MediaStore.Images.ImageColumns.MIME_TYPE
////                    };
////                    final Cursor cursor = getContentResolver()
////                            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
////                                    null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
////                    cursor.moveToLast();
////                    String imageLocation = cursor.getString(1);
////                    File imageFile = new File(imageLocation);
////                    if (imageFile.exists()) {
////                            Log.d("Paint Activity", "onClick: PaintActivity" + imageLocation);
////                    }
////
////
////                    if(imgSaved!=null){
////                        Toast.makeText(PaintActivity.this, "Image is saved", Toast.LENGTH_SHORT).show();
////                    }else{
////                        Toast.makeText(PaintActivity.this, "Image could not be saved", Toast.LENGTH_SHORT).show();
////                        drawingView.destroyDrawingCache();
////                    }
//                }
//            });
//            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            saveDialog.show();
        }



    }
    private File getFile(){
        File folder = new File("sdcard/pictures");
        if(!folder.exists()){
            folder.mkdir();
        }
//            int r = new Random().nextInt(90000-10000 + 1) + 10000;
//            currentCamImage =  "cam_image" + r + ".jpg";
        File imageFile = new File(folder, "");

        return imageFile;
    }
}
