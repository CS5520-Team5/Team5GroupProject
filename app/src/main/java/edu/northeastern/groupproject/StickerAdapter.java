package edu.northeastern.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    private List<Sticker> stickerList;
    private Context context;

    public StickerAdapter(List<Sticker> stickerList, Context context) {
        this.stickerList = stickerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sticker_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sticker sticker = stickerList.get(position);
        holder.username.setText(sticker.getRecipient());
        holder.time.setText(formatTime(sticker.getTime()));
        if (sticker.getImageId().equals("like")){
            Glide.with(context).asGif().load(R.drawable.ic_like).override(100,100).into(holder.image);
        } else if (sticker.getImageId().equals("kiss")){
            Glide.with(context).asGif().load(R.drawable.ic_kiss).override(100,100).into(holder.image);
        }else if (sticker.getImageId().equals("think")){
            Glide.with(context).asGif().load(R.drawable.ic_think).override(100,100).into(holder.image);
        }else if (sticker.getImageId().equals("happy")){
            Glide.with(context).asGif().load(R.drawable.ic_happy).override(100,100).into(holder.image);
        }else if (sticker.getImageId().equals("wipe")){
            Glide.with(context).asGif().load(R.drawable.ic_wipe).override(100,100).into(holder.image);
        }else if (sticker.getImageId().equals("star")){
            Glide.with(context).asGif().load(R.drawable.ic_star).override(100,100).into(holder.image);
        }

    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public String formatTime(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return simpleDateFormat.format(new Date(time));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView time;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
        }

        public TextView getUsername() {
            return this.username;
        }

        public TextView getTime() {
            return this.time;
        }

        public ImageView getImage() {
            return this.image;
        }
    }
}
