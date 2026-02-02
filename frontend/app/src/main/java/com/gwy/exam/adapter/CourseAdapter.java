package com.gwy.exam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gwy.exam.R;
import com.gwy.exam.model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private OnCourseClickListener listener;

    public interface OnCourseClickListener {
        void onCourseClick(Course course);
    }

    public CourseAdapter() {
        this.courseList = new ArrayList<>();
    }

    public void setCourses(List<Course> courses) {
        this.courseList = courses;
        notifyDataSetChanged();
    }

    public void setOnCourseClickListener(OnCourseClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.bind(course);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseTitle;
        TextView tvCourseSubtitle;
        TextView tvCourseDuration;
        TextView tvCourseLessons;
        TextView tvCoursePrice;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseTitle = itemView.findViewById(R.id.tv_course_title);
            tvCourseSubtitle = itemView.findViewById(R.id.tv_course_subtitle);
            tvCourseDuration = itemView.findViewById(R.id.tv_course_duration);
            tvCourseLessons = itemView.findViewById(R.id.tv_course_lessons);
            tvCoursePrice = itemView.findViewById(R.id.tv_course_price);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCourseClick(courseList.get(position));
                    }
                }
            });
        }

        public void bind(Course course) {
            tvCourseTitle.setText(course.getTitle());
            tvCourseSubtitle.setText(course.getDescription());
            tvCourseDuration.setText(course.getDuration() + "课时");
            tvCourseLessons.setText("| " + course.getLessonCount() + "讲");
            tvCoursePrice.setText("¥" + course.getPrice());
        }
    }
}