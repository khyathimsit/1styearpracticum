package com.example.wingman;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class changesilentmode extends AppCompatActivity {

    private AudioManager myAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changesilentmode);
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        String in = getIntent().getStringExtra("value");
        if(in.equals("silent")){

            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else if(in.equals("vibrate")){
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else if(in.equals("normal")){
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

}
