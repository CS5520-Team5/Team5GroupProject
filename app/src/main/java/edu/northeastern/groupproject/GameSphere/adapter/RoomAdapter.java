package edu.northeastern.groupproject.GameSphere.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.northeastern.groupproject.GameSphere.model.Room;
import edu.northeastern.groupproject.R;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private List<Room> roomList;
    private Context context;

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_room_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, int position) {
        Room room= roomList.get(position);
        holder.room_name.setText(room.getRoomName());
        holder.room_desc.setText(room.getRoomDescription());
        holder.room_num.setText(room.getMembers().size());
        Glide.with(context).load(room.getImage()).into(holder.room_image);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView room_image;
        private ImageView admin_user_avatar;
        private TextView room_name;
        private TextView room_desc;
        private TextView room_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_image=itemView.findViewById(R.id.room_image);
            admin_user_avatar=itemView.findViewById(R.id.admin_user_avatar);
            room_name=itemView.findViewById(R.id.room_name);
            room_desc=itemView.findViewById(R.id.room_desc);
            room_num=itemView.findViewById(R.id.room_num);
        }
    }
}