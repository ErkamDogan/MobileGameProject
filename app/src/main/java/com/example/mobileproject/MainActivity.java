package com.example.mobileproject;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Play Game button
        ((Button) findViewById(R.id.playgame)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this,Start.class);
                startActivity(t);
            }
        });
        // Credits button
        ((Button) findViewById(R.id.credits)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this,Credits.class);
                startActivity(t);
            }
        });
        // Options button
        ((Button) findViewById(R.id.Options)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(MainActivity.this,Options.class);
                startActivity(t);
            }
        });
        // Exit button
        ((Button) findViewById(R.id.Exit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("info", "MainActivity onResume");
        super.onResume();

    }

}
