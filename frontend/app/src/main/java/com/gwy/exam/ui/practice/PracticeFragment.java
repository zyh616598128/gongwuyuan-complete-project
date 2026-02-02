package com.gwy.exam.ui.practice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwy.exam.R;

public class PracticeFragment extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_practice, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_practice);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup practice options adapter
        // PracticeOptionsAdapter adapter = new PracticeOptionsAdapter();
        // recyclerView.setAdapter(adapter);

        return root;
    }
}