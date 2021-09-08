package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CreateButtonMenu extends AppCompatActivity {

    Button createBtn;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_button_menu);

        createBtn = findViewById(R.id.createBtn);
        etName = findViewById(R.id.btnNameEt);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Create Button");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}