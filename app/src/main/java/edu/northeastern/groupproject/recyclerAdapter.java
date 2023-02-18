package edu.northeastern.groupproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<Response> responseList;

    public recyclerAdapter(ArrayList<Response> responseList){
        this.responseList = responseList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView picText;

        public MyViewHolder(final View view){
            super(view);
            picText = view.findViewById(R.id.textView);
        }
    }




    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_individual, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = responseList.get(position).getPic();
        holder.picText.setText(name);

    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }
}
