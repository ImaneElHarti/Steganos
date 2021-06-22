package com.elharti.steganos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        TextView t = (TextView)findViewById(R.id.t);
        t.setText("This work has been realized in order to present it as a final project, by the students " +
                  "of the degree course in Mathematics " +
                  "and computer science at the faculty of science Rabat Morocco: \n" +
                  "El Harti Imane, Elharti Adnan under the supervision of Mr. El Mamoun Souidi.");


        TextView t2 = (TextView)findViewById(R.id.t2);
        t2.setText("CONTACT : \n" +
                "\t \t El Harti Imane: " +
                "\n \t \t \t elharti.imane.98@gmail.com " +
                "\n \t \t \t 0610775099 \n" +
                "\t \t Elharti Adnan: " +
                "\n \t \t \t adnanelharti2@gmail.com" +
                "\n \t \t \t 0644896608 \n " +
                "\t \t El Mamoun Souidi: " +
                "\n \t \t \t emsouidi@gmail.com");

    }

}