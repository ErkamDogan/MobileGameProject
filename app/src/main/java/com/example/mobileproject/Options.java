package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Options extends AppCompatActivity {
    MediaPlayer ply;
    SeekBar change;
    AudioManager manager;
    Boolean isOpen=false;
    int runningtype=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        change=(SeekBar)findViewById(R.id.seekBar);
        manager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        setSeekBar(change,AudioManager.STREAM_MUSIC);

        //Main Menu Button
        ((Button) findViewById(R.id.mainmenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Options.this,MainActivity.class);
                startActivity(t);
            }
        });
    }

    private void setSeekBar(SeekBar bar,final int stream) {
        bar.setMax(manager.getStreamMaxVolume(stream));
        bar.setProgress(manager.getStreamVolume(stream));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(stream,progress,R.raw.songone);
                manager.setStreamVolume(stream,progress,R.raw.songtwo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDestroy() {
        ply.stop();
        ply.release();
    }
    // Emotional button, if click the button, it start emotional song.
    public void playemotional(View view) {
        // If song is playing, song stops. If other song is playing, it stops and emotional song starts.
        if(!isOpen || runningtype == 2){
            if(isOpen){
                ply.stop();
            }
            ply= MediaPlayer.create(Options.this,R.raw.songone);
            ply.start();
            isOpen=true;
            runningtype = 1;
        }
        else if(isOpen){
            ply.stop();
            isOpen = false;
        }
    }

    public void playpeacefull(View view) {
        if(!isOpen || runningtype == 1){
            if(isOpen){
                ply.stop();
            }
            ply= MediaPlayer.create(Options.this,R.raw.songtwo);
            ply.start();
            isOpen=true;
            runningtype = 2;
        }
        else if(isOpen){
            ply.stop();
            isOpen = false;
        }
    }

}
