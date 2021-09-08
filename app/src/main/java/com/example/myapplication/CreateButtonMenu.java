package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateButtonMenu extends AppCompatActivity {

    Button createBtn;
    EditText etName;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_button_menu);

        createBtn = findViewById(R.id.createBtn);
        etName = findViewById(R.id.btnNameEt);

        dbHelper = new DataBaseHelper(CreateButtonMenu.this);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Create Button");
        }
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CreateButtonMenu.this, MainActivity.class);
            ButtonModel buttonModel;
            try {
                if(etName.getText().toString().matches("")) throw new NoButtonNameException("Forgot to give the button a name");
                buttonModel = new ButtonModel(-1, etName.getText().toString(), "path");
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
    }
}