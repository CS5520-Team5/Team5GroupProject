package edu.northeastern.groupproject.GameSphere;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.northeastern.groupproject.R;

public class PostFragment extends Fragment {
    RecyclerView postRecycler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_post_fragment, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // hooks
//        postRecycler=view.findViewById(R.id.post_recycler);
        setPostRecycler();
    }
    private void setPostRecycler(){
//        postRecycler.setHasFixedSize(true);
//        postRecycler.setLayoutManager(new LinearLayoutManager( this,LinearLayoutManager.HORIZONTAL,false));

    }
}