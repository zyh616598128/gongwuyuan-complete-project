package com.gwy.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gwy.exam.adapter.CourseAdapter;
import com.gwy.exam.model.Course;
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
        loadCourseData();
        
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

    private void loadCourseData() {
        // 模拟课程数据
        courseList.add(new Course(1L, "行政职业能力测验精讲班", 
                "系统讲解行测五大模块，掌握核心解题技巧", 
                "", "李老师", 199.0, 24, 12, "行测", "video", 4.8, 1250, false));
        
        courseList.add(new Course(2L, "申论写作技巧提升班", 
                "从审题到写作，全面提升申论应试能力", 
                "", "王老师", 299.0, 32, 16, "申论", "video", 4.9, 980, false));
        
        courseList.add(new Course(3L, "公共基础知识系统班", 
                "覆盖政治、经济、法律、历史等全部考点", 
                "", "张老师", 259.0, 40, 20, "公基", "video", 4.7, 1560, false));
        
        courseList.add(new Course(4L, "面试技巧实战班", 
                "结构化面试技巧，模拟实战演练", 
                "", "刘老师", 399.0, 28, 14, "面试", "live", 4.9, 750, false));
        
        courseAdapter.setCourses(courseList);
    }

    @Override
    public void onCourseClick(Course course) {
        // 处理课程点击事件
        // 可以跳转到课程详情页面
    }
}