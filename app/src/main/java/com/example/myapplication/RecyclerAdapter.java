package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<ButtonModel> btnList;

    public RecyclerAdapter(ArrayList<ButtonModel> btnList) {
        this.btnList = btnList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private Button button;
        private TextView textView;
        private String path;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            textView = itemView.findViewById(R.id.textView);
            path = "";

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "path: " + path, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_button, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        int id = btnList.get(position).getId();
        String name = btnList.get(position).getName();
        String path = btnList.get(position).getPath();
        holder.button.setText(String.valueOf(id));
        holder.textView.setText(name);
        holder.path = path;
    }

    @Override
    public int getItemCount() {
        return btnList.size();
    }
}
