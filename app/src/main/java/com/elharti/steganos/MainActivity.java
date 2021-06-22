package com.elharti.steganos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    final int GET_FROM_GALLERY = 1;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        image = (ImageView) findViewById(R.id.image);
        Button parcourir = (Button) findViewById(R.id.parcourir);
        Button encoder = (Button) findViewById(R.id.insert);
        Button decoder = (Button) findViewById(R.id.decoder);
        parcourir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });

        encoder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent EncoderT = new Intent(MainActivity.this, choice.class);
                BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();
                Bitmap bitmapSupport = bitmap.getBitmap();
                createImageFromBitmap(bitmapSupport, "imageSupport");
                startActivity(EncoderT);
            }
        });
        decoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent verif = new Intent(MainActivity.this, verificationMdp.class);
                BitmapDrawable bitmap = (BitmapDrawable) image.getDrawable();
                Bitmap bitmapSupport = bitmap.getBitmap();
                createImageFromBitmap(bitmapSupport, "imageSupport");

                startActivity(verif);
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.about_us==item.getItemId()){
            Intent aboutus = new Intent(MainActivity.this, aboutus.class);
            startActivity(aboutus);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);
        }

    }

    public String createImageFromBitmap(Bitmap bitmap, String s) {
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

}