package edu.northeastern.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.getUsername().setText(sticker.getSender());
        holder.getTime().setText(formatTime(sticker.getTime()));
        String imageId = sticker.getImageId();
        // TODO: Replace placeholders with sticker images and IDs
//        switch (imageId) {
//            case "PLACEHOLDER1":
//                holder.image.setImageResource(R.drawable.PLACEHOLDER1);
//                break;
//            case "PLACEHOLDER2":
//                holder.image.setImageResource(R.drawable.PLACEHOLDER2);
//                break;
//            case "PLACEHOLDER3":
//                holder.image.setImageResource(R.drawable.PLACEHOLDER3);
//                break;
//        }
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
