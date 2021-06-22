package com.elharti.steganos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class exractImage extends AppCompatActivity {
    private int STORAGE_PERMISSION_CODE = 1;
    public Bitmap bitmapSupport=null ;
    ImageView image;
    Button telecharger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exract_image);
        image = (ImageView)findViewById(R.id.imageSecrete);
        telecharger =(Button)findViewById(R.id.telecharger);
        FileInputStream fi = null;
        try {
            fi = openFileInput("imageSupport");
        }catch (IOException e){
            e.printStackTrace();

        }
        bitmapSupport = BitmapFactory.decodeStream(fi);
       extraire(bitmapSupport);

        telecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(exractImage.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(exractImage.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }

                String root = Environment.getExternalStorageDirectory().toString();
                File mDossier = new File(root+"/Download");
                mDossier.mkdirs();

                String nom=" image_secrete.png";
                File file = new File(mDossier,nom);
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    BitmapDrawable imageSecreteD = (BitmapDrawable)image.getDrawable();
                    Bitmap imageSecrete = imageSecreteD.getBitmap();
                    imageSecrete.compress(Bitmap.CompressFormat.PNG,100,out);
                    out.flush();
                    out.close();
                    Toast.makeText(exractImage.this,"succesfully downloaded",Toast.LENGTH_SHORT).show();
                }catch (Exception e){

                }


            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for downloading your result image")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(exractImage.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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




        public void extraire(Bitmap bitmap){
            int p1 = bitmap.getPixel(bitmap.getWidth()-1,bitmap.getHeight()-2);
            int p2=bitmap.getPixel(bitmap.getWidth()-1,bitmap.getHeight()-3);
            int p3=bitmap.getPixel(bitmap.getWidth()-1,bitmap.getHeight()-4);
            int p4=bitmap.getPixel(bitmap.getWidth()-1,bitmap.getHeight()-5);
            int a=Color.alpha(p1),b=Color.blue(p1),c=Color.alpha(p2),d=Color.blue(p2);
            int w=Color.alpha(p1)+255*(Color.alpha(p3))+2;
            int h=Color.alpha(p2)+255*(Color.alpha(p4))+2;
            int l=0;
            String s="";
            Bitmap operation = Bitmap.createBitmap(w,h,bitmap.getConfig());
            int k = 0;
            for(int i=0;i<w;i++){
                for(int j=0;j<h;j++){
                    p1 = bitmap.getPixel(i,j);
                    int r1= Color.red(p1);
                    int g1= Color.green(p1);
                    int b1= Color.blue(p1);
                    r1=r1&15;
                    g1=g1&15;
                    b1=b1&15;
                    r1=r1<<4;
                    g1=g1<<4;
                    b1=b1<<4;
                    operation.setPixel(i,j,Color.argb(255,r1|3,g1|3,b1|3));
                }
            }
        image.setImageBitmap(operation);
        }

}
