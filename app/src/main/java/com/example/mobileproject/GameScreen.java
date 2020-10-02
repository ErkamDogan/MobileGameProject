package com.example.mobileproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class GameScreen extends Activity {
    static GameScreen activity;
    private SharedPreferences sharedPreferences;
    private Map drawingView;
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingView = new Map(this);
        setContentView(drawingView);
        activity = this;
        globals = Globals.getInstance();
        sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        int temp = sharedPreferences.getInt("high_score",0);
        globals.setHighScore(temp);
    }

    @Override
    protected void onPause() {
        Log.i("info", "onPause");
        super.onPause();
        drawingView.pause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("high_score", globals.getHighScore());
        editor.apply();
    }

    @Override
    protected void onResume() {
        Log.i("info", "onResume");
        super.onResume();
        drawingView.resume();

    }

    public static GameScreen getInstance() {
        return activity;
    }

}
