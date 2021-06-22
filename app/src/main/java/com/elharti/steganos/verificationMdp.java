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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class verificationMdp extends AppCompatActivity {
    Bitmap bitmapSupport;
    int cpt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_mdp);
        final Button extraire = (Button)findViewById(R.id.extract);
        final EditText mdp = (EditText)findViewById(R.id.mdp);
        final TextView cpt = (TextView)findViewById(R.id.cpt);
        final TextView erreur = (TextView)findViewById(R.id.Error);
        cpt.setText(0+"/"+6);
        mdp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int l=mdp.getText().length();
                cpt.setText(l+"/"+6);
                if(l>6){
                    cpt.setTextColor(Color.RED);
                    mdp.setTextColor(Color.RED);
                    extraire.setEnabled(false);
                }
                else{
                    cpt.setTextColor(Color.GRAY);
                    mdp.setTextColor(Color.BLACK);
                    extraire.setEnabled(true);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        FileInputStream fi = null;
        try {
            fi = openFileInput("imageSupport");
        }catch (IOException e){
            e.printStackTrace();

        }
        bitmapSupport = BitmapFactory.decodeStream(fi);

        extraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="";
                for(int i=1;i<7;i++){
                    int p =bitmapSupport.getPixel(i,PosMdp(i));
                    int alpha = Color.alpha(p);
                    s+=(char)alpha;
                }

                if(s.equals(mdp.getText().toString())){
                    Intent decoder=null;
                    if (Color.alpha(bitmapSupport.getPixel(bitmapSupport.getWidth()-1,bitmapSupport.getHeight()-1))==0) {


                        decoder = new Intent(verificationMdp.this, Decoder.class);
                        createImageFromBitmap(bitmapSupport,"imageSupport");
                        startActivity(decoder);
                    }
                    else{
                        decoder = new Intent(verificationMdp.this, exractImage.class);
                        createImageFromBitmap(bitmapSupport,"imageSupport");
                        startActivity(decoder);
                    }

                }
                else {
                    erreur.setTextColor(Color.RED);
                    erreur.setText("password error, try again");
                    mdp.setText("");
                    mdp.setTextColor(Color.RED);
                }
            }
        });
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
    int PosMdp(int x){
        return 2*x*x+1;
    }
}