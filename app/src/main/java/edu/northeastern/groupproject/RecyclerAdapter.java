package edu.northeastern.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private ArrayList<Joke> jokeList;
    private Context context;

    public RecyclerAdapter(ArrayList<Joke> jokeList, Context context) {
        this.jokeList = jokeList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_individual,null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String category=jokeList.get(position).getCategory();
        holder.text_category.setText(category);
        holder.text_setup.setText(jokeList.get(position).getSetup());
        holder.text_delivery.setText(jokeList.get(position).getDelivery());
        switch (category){
            case "Programming":
                holder.image_category.setImageResource(R.drawable.programming);
            case "Misc":
                holder.image_category.setImageResource(R.drawable.misc);
            case "Dark":
                holder.image_category.setImageResource(R.drawable.dark);
            case "Pun":
                holder.image_category.setImageResource(R.drawable.pun);
            case "Spooky":
                holder.image_category.setImageResource(R.drawable.spooky);
            case "Christmas":
                holder.image_category.setImageResource(R.drawable.christmas);
        }

    }

    @Override
    public int getItemCount() {
        return jokeList.size();
    }
}
