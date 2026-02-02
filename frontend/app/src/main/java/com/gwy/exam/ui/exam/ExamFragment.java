package com.gwy.exam.ui.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gwy.exam.R;

public class ExamFragment extends Fragment {

    private RecyclerView recyclerViewExams;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exam, container, false);

        recyclerViewExams = root.findViewById(R.id.recycler_view_exams);
        recyclerViewExams.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup exams adapter
        // ExamsAdapter adapter = new ExamsAdapter();
        // recyclerViewExams.setAdapter(adapter);

        return root;
    }
}