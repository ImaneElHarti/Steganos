package com.elharti.steganos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

public class ResultatIntro extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    Bitmap bitmapSupport;
    ImageView imageR;
    EditText fileName ;
    String name ="";
     Button telecharger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_intro);
        fileName = (EditText)findViewById(R.id.fileName);
        imageR =(ImageView)findViewById(R.id.imageR);
        telecharger = (Button)findViewById(R.id.telecharger);
        telecharger.setEnabled(false);
        fileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                name = fileName.getText().toString();
                if (name.length()==0){
                    telecharger.setEnabled(false);
                }else{
                    telecharger.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

        TextView mdp = (TextView) findViewById(R.id.mdp);
        mdp.setTextIsSelectable(true);
        FileInputStream fi = null;
        try {
            fi = openFileInput("imageResultat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        bitmapSupport = BitmapFactory.decodeStream(fi);

        imageR.setImageBitmap(bitmapSupport);



        String s="";
        for(int i=1;i<7;i++){
            int p =bitmapSupport.getPixel(i,PosMdp(i));
            int alpha = Color.alpha(p);
            s+=(char)alpha;
        }
        mdp.setText("Your password : "+s);



        telecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ResultatIntro.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } else {
                     String root = Environment.getExternalStorageDirectory().toString();
                File mDossier = new File(root+"/Download");
                mDossier.mkdirs();

                String nom= name+".png";
                File file = new File(mDossier,nom);
                try{
                    FileOutputStream out = new FileOutputStream(file);

                    bitmapSupport.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                    Toast.makeText(ResultatIntro.this,"Succefully downloaded",Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }

                }               

            }
        });

    }
    int PosMdp(int x){
        return 2*x*x+1;
    }
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for downloading your result image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ResultatIntro.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
