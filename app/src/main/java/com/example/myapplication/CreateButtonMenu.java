package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateButtonMenu extends AppCompatActivity {

    private static final int MICROPHONE_PERMISSION = 200;
    private static final int AUDIO_TIME_LIMIT = 5000; //milliseconds
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    Button createBtn;
    EditText etName;
    Button recordBtn;
    Button playBtn;

    DataBaseHelper dbHelper;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AtomicBoolean isPlayingAudio = new AtomicBoolean(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_button_menu);

        createBtn = findViewById(R.id.createBtn);
        etName = findViewById(R.id.btnNameEt);
        recordBtn = findViewById(R.id.recordBtn);
        playBtn = findViewById(R.id.playBtn);

        dbHelper = new DataBaseHelper(CreateButtonMenu.this);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Create Button");
        }
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(isMicrophonePresent()) getMicrophonePermission();

        createBtn.setOnClickListener(view -> {
            try {
                getRecordingFilePath();
            } catch (NoButtonNameException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(CreateButtonMenu.this, MainActivity.class);
            ButtonModel buttonModel;
            try {
                if(etName.getText().toString().matches("")) throw new NoButtonNameException("Forgot to give the button a name");
                buttonModel = new ButtonModel(-1, etName.getText().toString(), file.getPath());
            }
            catch (Exception e){
                Toast.makeText(CreateButtonMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            etName.setText("");
            boolean success = dbHelper.addOne(buttonModel);
            Toast.makeText(CreateButtonMenu.this, "Adding button: " + success + "\n" + buttonModel.toString(), Toast.LENGTH_SHORT).show();
            if(success) startActivity(intent);
        });

        recordBtn.setOnClickListener(view -> {
            if(isPlayingAudio.get()){
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isPlayingAudio.set(false);
                Toast.makeText(this, "Recording is stopped", Toast.LENGTH_SHORT).show();
            }
            else try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setOutputFile(getRecordingFilePath());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this, "Recording is started", Toast.LENGTH_SHORT).show();
                isPlayingAudio.set(true);
                new Handler().postDelayed(() -> {if(isPlayingAudio.get()) recordBtn.performClick();}, AUDIO_TIME_LIMIT);
            } catch(Exception e){
                Toast.makeText(this, "Failed to record audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        playBtn.setOnClickListener(view -> {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(getRecordingFilePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(this, "Recording is playing", Toast.LENGTH_SHORT).show();
            } catch(Exception e){
                Toast.makeText(this, "Failed to play audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isMicrophonePresent() {
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION);
        }
    }

    private String getRecordingFilePath() throws NoButtonNameException {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if(etName.getText().toString().matches("")) throw new NoButtonNameException("Forgot to give the button a name");
        file  = new File(musicDirectory, etName.getText() + ".mp3");
        //Toast.makeText(this, "path: " + file.getPath(), Toast.LENGTH_SHORT).show();
        return file.getPath();
    }
}