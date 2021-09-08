package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RecyclerView btnList;
    Button createBtn;

    Button btnDeleteAll;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnList = findViewById(R.id.rclView);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        createBtn = findViewById(R.id.createBtnMenu);

        dbHelper = new DataBaseHelper(MainActivity.this);

        showButtonList();

        btnDeleteAll.setOnClickListener(view -> {
            boolean success = dbHelper.restartTable();
            Toast.makeText(MainActivity.this, "Table restarted: " + success, Toast.LENGTH_SHORT).show();
            showButtonList();
        });

        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateButtonMenu.class);
            startActivity(intent);
        });
    }

    private void showButtonList(){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(dbHelper.getAll());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        btnList.setLayoutManager(layoutManager);
        btnList.setItemAnimator(new DefaultItemAnimator());
        btnList.setAdapter(recyclerAdapter);
    }
}