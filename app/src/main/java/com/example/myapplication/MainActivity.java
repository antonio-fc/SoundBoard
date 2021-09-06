package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText btnName;
    RecyclerView btnList;

    Button btnDeleteAll;

    DataBaseHelper dbHelper;
    ArrayAdapter btnArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnName = findViewById(R.id.etName);
        btnList = findViewById(R.id.rclView);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        dbHelper = new DataBaseHelper(MainActivity.this);

        showButtonList();

        btnAdd.setOnClickListener(view -> {
            ButtonModel buttonModel;
            try {
                if(btnName.getText().toString().matches("")) throw new NoButtonNameException("Forgot to give the button a name");
                buttonModel = new ButtonModel(-1, btnName.getText().toString(), "path");
            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            btnName.setText("");
            boolean success = dbHelper.addOne(buttonModel);
            Toast.makeText(MainActivity.this, "Adding button: " + success + "\n" + buttonModel.toString(), Toast.LENGTH_SHORT).show();
            showButtonList();
        });

        btnDeleteAll.setOnClickListener(view -> {
            boolean success = dbHelper.restartTable();
            Toast.makeText(MainActivity.this, "Table restarted: " + success, Toast.LENGTH_SHORT).show();
            showButtonList();
        });
    }

    private void showButtonList(){
        //btnArrayAdapter = new ArrayAdapter<ButtonModel>(MainActivity.this, android.R.layout.simple_list_item_1, dbHelper.getAll());
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(dbHelper.getAll());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        btnList.setLayoutManager(layoutManager);
        btnList.setItemAnimator(new DefaultItemAnimator());
        btnList.setAdapter(recyclerAdapter);
    }
}