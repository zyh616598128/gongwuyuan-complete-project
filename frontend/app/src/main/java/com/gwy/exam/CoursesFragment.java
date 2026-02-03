package com.gwy.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gwy.exam.adapter.CourseAdapter;
import com.gwy.exam.model.Course;
import com.gwy.exam.api.ApiClient;
import com.gwy.exam.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment implements CourseAdapter.OnCourseClickListener {

    private RecyclerView recyclerViewCourses;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadCourseDataFromApi();
        
        return view;
    }

    private void initViews(View view) {
        recyclerViewCourses = view.findViewById(R.id.recycler_view_courses);
    }

    private void setupRecyclerView() {
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter();
        courseAdapter.setOnCourseClickListener(this);
        
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCourses.setAdapter(courseAdapter);
    }

    private void loadCourseDataFromApi() {
        // Currently, we don't have courses API in the backend
        // We'll need to implement a courses API or use existing data
        // For now, showing a toast that this feature will be implemented
        Toast.makeText(getContext(), "课程数据将在后续版本中提供", Toast.LENGTH_SHORT).show();
        
        // For now, we'll still initialize with an empty list
        courseAdapter.setCourses(courseList);
    }

    @Override
    public void onCourseClick(Course course) {
        // 处理课程点击事件
        // 可以跳转到课程详情页面
    }
}