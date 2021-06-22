package com.elharti.steganos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.IOException;

public class Decoder extends AppCompatActivity {
    TextView text;
    Bitmap bitmapSupport ;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);
        FileInputStream fi = null;
        try {
            fi = openFileInput("imageSupport");
        }catch (IOException e){
            e.printStackTrace();

        }
        bitmapSupport = BitmapFactory.decodeStream(fi);
        text  =(TextView)findViewById(R.id.secret);
        extraire(bitmapSupport);
    }
    public void extraire(Bitmap bitmap){
        String message="";

        int p=  bitmap.getPixel(0,0);
        int taille = Color.alpha(p);
        int c =0;
        for(int i=0;i<bitmapSupport.getHeight();i++){
            for(int j=0;j<bitmapSupport.getWidth();j++){
                if((i!=0 || j!=0 ) && PosMdp(i)!=j){
                    if (c<taille){
                        p= bitmap.getPixel(i,j);
                        int alpha = Color.alpha(p);
                        message += ""+ (char)alpha;
                        c++;}
                    else break;
                }
            }
        }
        text.setText(message);
}
int PosMdp(int x){
        return 2*x*x+1;
    }
}
