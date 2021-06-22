package com.elharti.steganos;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Encoder extends AppCompatActivity {

    final int GET_FROM_GALLERY = 1;
    ImageView image;
    Button parcourir;
    Button insert;
    TextView taille;
    Bitmap imageSecrete;
    int a ;
    int l;
    public  static  Bitmap bitmapSupport=null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoder);
        image = (ImageView) findViewById(R.id.image);
        parcourir = (Button) findViewById(R.id.parcourir);
        insert =(Button)findViewById(R.id.insert);


        FileInputStream fi =null;
        try {
             fi = openFileInput("imageSupport");


        }catch (IOException e){
            e.printStackTrace();

        }
        bitmapSupport = BitmapFactory.decodeStream(fi);
        a=(bitmapSupport.getHeight()*bitmapSupport.getWidth()-10);
        BitmapDrawable imageSecreteD = (BitmapDrawable)image.getDrawable();
         imageSecrete = imageSecreteD.getBitmap();



        l = (imageSecrete.getHeight()*imageSecrete.getWidth());

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l>a){
                    Toast.makeText(Encoder.this, "Invalid Size", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent EncoderT = new Intent(Encoder.this,ResultatIntro.class);
                    BitmapDrawable imageSecreteD = (BitmapDrawable)image.getDrawable();
                    imageSecrete = imageSecreteD.getBitmap();
                    bitmapSupport = introduire(imageSecrete,bitmapSupport);
                    createImageFromBitmap(bitmapSupport,"imageResultat");
                    startActivity(EncoderT);
                }

            }
        });



        parcourir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode== GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);

        }

    }


    public String createImageFromBitmap(Bitmap bitmap,String s) {
        String fileName = s;//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public Bitmap introduire(Bitmap imageSecrete, Bitmap bitmap) {
        String s = mdp();
        Bitmap operation = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        int h=imageSecrete.getHeight();
        int w= imageSecrete.getWidth();
        int hs=bitmap.getHeight();
        int ws=bitmap.getWidth();
        int p1;
        int r1;
        int g1;
        int b1;
        int p2;
        int r2;
        int g2;
        int b2;

        for(int i=0; i<ws; i++){
            for(int j=0; j<hs;j++) {

                p1 = bitmap.getPixel(i,j);
                r1 = Color.red(p1);
                g1 = Color.green(p1);
                b1 = Color.blue(p1);
                int alpha1 = Color.alpha(p1);

                     if (i < w && j < h) {
                              r1=r1&240;
                              g1=g1&240;
                              b1=b1&240;
                              p2 = imageSecrete.getPixel(i,j);
                              r2 = Color.red(p2);
                              g2 = Color.green(p2);
                              b2 = Color.blue(p2);
                              r2= r2>>4;
                              g2= g2>>4;
                              b2= b2>>4;

                              operation.setPixel(i,j,Color.argb(255,r1|r2,g1|g2,b1|b2));
                    }
                else {
                    operation.setPixel(i, j, Color.argb(alpha1, r1, g1, b1));
                }


        }
     }
        p1 = bitmap.getPixel(ws-1,hs-1);
        r1 = Color.red(p1);
        g1 = Color.green(p1);
        b1 = Color.blue(p1);

            operation.setPixel(ws-1,hs-1,Color.argb(1,r1,g1,b1));

        for(int i=1;i<7;i++){
            p1 = bitmap.getPixel(i,PosMdp(i));
            r1 = Color.red(p1);
            g1 = Color.green(p1);
            b1 = Color.blue(p1);
            operation.setPixel(i, PosMdp(i), Color.argb(s.charAt(i-1), r1, g1, b1));
        }

            int t=h/255;
            int g=w/255;
            operation.setPixel(ws-1, hs-2, Color.argb(h, r1, g1,b1));
            operation.setPixel(ws-1,hs-3,Color.argb(w,r1,g1,b1));
            operation.setPixel(ws-1,hs-4,Color.argb(t,r1,g1,b1));
            operation.setPixel(ws-1,hs-5,Color.argb(g,r1,g1,b1));
        return operation;
    }
    public String mdp(){
        String s="";

        int k=0;
        while(k<6){
            int random = new Random().nextInt(75)+48;
            if((random>='0' &&  random<='0'+9)||(random>='A'&& random<='A'+25 )|| (random>='a'&&random<='a'+25)){
                s+=(char)random;
                k++;

            }
        }
        return  s;
    }

    int PosMdp(int x){
        return 2*x*x+1;
    }
}


