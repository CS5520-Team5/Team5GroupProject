package edu.northeastern.groupproject.GameSphere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.groupproject.GameSphere.model.User;
import edu.northeastern.groupproject.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_user_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user=userList.get(position);
        // TODO: bind
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatar;
        private TextView username;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar=itemView.findViewById(R.id.avatar);
            username=itemView.findViewById(R.id.username);
        }
    }
}
