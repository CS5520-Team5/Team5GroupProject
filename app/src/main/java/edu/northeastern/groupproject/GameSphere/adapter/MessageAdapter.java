package edu.northeastern.groupproject.GameSphere.adapter;

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
import java.util.Locale;

import edu.northeastern.groupproject.GameSphere.model.Message;
import edu.northeastern.groupproject.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<Message> messageList;
    private Context context;

    public MessageAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapter.MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_message_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message=messageList.get(position);
        holder.username.setText(String.valueOf(message.getSender()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(message.getTime());
        String formattedDate = sdf.format(date);
        holder.timestamp.setText(formattedDate);
        holder.messageContent.setText(String.valueOf(message.getContent()));
        Glide.with(context).load(String.valueOf(message.getAvatar())).centerCrop().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView timestamp;
        private TextView messageContent;
        private ImageView avatar;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.sender);
            timestamp=itemView.findViewById(R.id.timestamp);
            messageContent=itemView.findViewById(R.id.message_content);
            avatar=itemView.findViewById(R.id.avatar);
        }
    }
}
