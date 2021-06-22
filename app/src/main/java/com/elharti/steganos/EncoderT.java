package com.elharti.steganos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class EncoderT extends AppCompatActivity {
    public Bitmap bitmapSupport = null;
    EditText text;
    String textSecret;
    TextView cpt ;
    int a=0,l=0;
    Button insert;
    String mdp="";
    EditText name;

    String nom="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoder_t);
        text = (EditText)findViewById(R.id.textSecret);
        insert =(Button)findViewById(R.id.insert);
        textSecret = text.getText().toString();
        FileInputStream fi=null;
        cpt= (TextView)findViewById(R.id.cpt);
        cpt.setText("0/"+a);
        try {
            fi = openFileInput("imageSupport");
             }catch (IOException e){
            e.printStackTrace();
            }

        bitmapSupport = BitmapFactory.decodeStream(fi);


        text.setOnClickListener(new View.OnClickListener() {
            int q =0;
            @Override
            public void onClick(View v) {
                if(q==0){
                text.setText("");
                q++;
                }
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mdp = mdp();
                String strName = null;
                bitmapSupport= introduire(text.getText().toString(),mdp,bitmapSupport);
                createImageFromBitmap(bitmapSupport,"imageResultat");
                Intent ResultatIntro = new Intent(EncoderT.this,ResultatIntro.class);
                startActivity(ResultatIntro);
            }
        });
        a=(bitmapSupport.getHeight()*bitmapSupport.getWidth()-1);
        cpt.setText(l+"/"+a);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                l=text.getText().length();
                cpt.setText(l+"/"+a);
                if(l>a){
                    cpt.setTextColor(Color.RED);
                    text.setTextColor(Color.RED);
                    insert.setEnabled(false);
                }
                else{
                    cpt.setTextColor(Color.GRAY);
                    text.setTextColor(Color.BLACK);
                    insert.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public Bitmap introduire(String textSecret,String mdp, Bitmap bitmap) {

        Bitmap operation = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
        int o=0;
        int taille = textSecret.length();
        int c =0;

        for(int i=0; i<bitmap.getWidth(); i++){
            for(int j=0; j<bitmap.getHeight();j++){

                int p = bitmap.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
    if((i!=0 || j!=0) &&   PosMdp(i)!=j ) {
        if(c<taille){
        int value = (int)textSecret.charAt(c);
        alpha = value;
        c++;}

        operation.setPixel(i, j, Color.argb(alpha, r, g, b));
    }
    else {
        if( o<6 ){
            if(i!=0) {
            operation.setPixel(i,j,Color.argb(mdp.charAt(o),r,g,b));
            o++;
        }


        else{

                operation.setPixel(i, j, Color.argb(textSecret.length(), r, g, b));
        }
        }
        else{

                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }

    }

                if(i==bitmap.getWidth()-1 && j==bitmap.getHeight()-1){
                    operation.setPixel(i,j,Color.argb(0,r,g,b));
                }
    }}


    return operation;
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
