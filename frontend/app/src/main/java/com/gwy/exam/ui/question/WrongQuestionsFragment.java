package com.gwy.exam.ui.question;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwy.exam.R;

public class WrongQuestionsFragment extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wrong_questions, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_wrong_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup wrong questions adapter
        // WrongQuestionsAdapter adapter = new WrongQuestionsAdapter();
        // recyclerView.setAdapter(adapter);

        return root;
    }
}