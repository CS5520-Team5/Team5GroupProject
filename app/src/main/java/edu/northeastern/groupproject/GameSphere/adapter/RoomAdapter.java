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

import edu.northeastern.groupproject.GameSphere.RoomClickListener;
import edu.northeastern.groupproject.GameSphere.model.Room;
import edu.northeastern.groupproject.R;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;
    private RoomClickListener roomClickListener;

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }
    public void setOnItemClickListener(RoomClickListener roomClickListener) {
        this.roomClickListener = roomClickListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_room_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        int position_saved=position;
        Room room= roomList.get(position);
        holder.room_name.setText(String.valueOf(room.getRoomName()));
        holder.room_desc.setText(String.valueOf(room.getRoomDescription()));
        holder.room_num.setText(String.valueOf(room.getMembers().size()));
        Glide.with(context).load(String.valueOf(room.getImage())).centerCrop().into(holder.room_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomClickListener!=null){
                    roomClickListener.onItemClick(position_saved);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        private ImageView room_image;
        private ImageView admin_user_avatar;
        private TextView room_name;
        private TextView room_desc;
        private TextView room_num;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            room_image=itemView.findViewById(R.id.room_image);
            admin_user_avatar=itemView.findViewById(R.id.admin_user_avatar);
            room_name=itemView.findViewById(R.id.room_name);
            room_desc=itemView.findViewById(R.id.room_desc);
            room_num=itemView.findViewById(R.id.room_num);
        }

    }
}