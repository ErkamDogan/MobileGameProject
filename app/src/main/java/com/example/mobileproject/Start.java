package com.example.mobileproject;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Start extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        final EditText nickname= (EditText) findViewById(R.id.Nickname);

        //ImageButtons
        //Left-up Button
        ((ImageButton) findViewById(R.id.upleft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Right-up Button
        ((ImageButton) findViewById(R.id.upright)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Left-down Button
        ((ImageButton) findViewById(R.id.downleft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Right-down Button
        ((ImageButton) findViewById(R.id.downright)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //Start Button
        ((Button) findViewById(R.id.Start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Start.this,GameScreen.class);
                startActivity(t);
            }
        });

    }
}
