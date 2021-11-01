package com.example.myapplication;

import android.media.MediaPlayer;
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
    private final ArrayList<ButtonModel> btnList;

    public RecyclerAdapter(ArrayList<ButtonModel> btnList) {
        this.btnList = btnList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        MediaPlayer mediaPlayer;

        private final Button button;
        private final TextView textView;
        private int id;
        private String path;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            textView = itemView.findViewById(R.id.textView);
            path = "";
            id = 0;

            button.setOnClickListener(view -> {
                //Toast.makeText(itemView.getContext(), "path: " + path, Toast.LENGTH_SHORT).show();
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(itemView.getContext(), "Recording is playing", Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Toast.makeText(itemView.getContext(), "Failed to play audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        holder.id = id;
        holder.path = path;
    }

    @Override
    public int getItemCount() {
        return btnList.size();
    }
}
