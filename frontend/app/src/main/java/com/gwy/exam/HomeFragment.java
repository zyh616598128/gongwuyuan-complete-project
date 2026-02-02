package com.gwy.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvStudyDays;
    private TextView tvTotalQuestions;
    private TextView tvAnsweredQuestions;
    private TextView tvAccuracyRate;
    private MaterialCardView cardPractice;
    private MaterialCardView cardRandom;
    private MaterialCardView cardExam;
    private MaterialCardView cardWrongBook;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserData();
        
        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvStudyDays = view.findViewById(R.id.tv_study_days);
        tvTotalQuestions = view.findViewById(R.id.tv_total_questions);
        tvAnsweredQuestions = view.findViewById(R.id.tv_answered_questions);
        tvAccuracyRate = view.findViewById(R.id.tv_accuracy_rate);
        
        // 快捷入口卡片
        cardPractice = view.findViewById(R.id.card_practice);
        cardRandom = view.findViewById(R.id.card_random);
        cardExam = view.findViewById(R.id.card_exam);
        cardWrongBook = view.findViewById(R.id.card_wrong_book);
    }

    private void setupClickListeners() {
        // 设置快捷入口点击事件
        cardPractice.setOnClickListener(v -> {
            // 跳转到顺序练习页面
            Intent intent = new Intent(getActivity(), PracticeActivity.class);
            startActivity(intent);
        });

        cardRandom.setOnClickListener(v -> {
            // 跳转到随机练习页面
            Intent intent = new Intent(getActivity(), PracticeActivity.class);
            startActivity(intent);
        });

        cardExam.setOnClickListener(v -> {
            // 跳转到模拟考试页面
            Intent intent = new Intent(getActivity(), MockExamActivity.class);
            startActivity(intent);
        });

        cardWrongBook.setOnClickListener(v -> {
            // 跳转到错题本页面
            Intent intent = new Intent(getActivity(), WrongBookActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        // 模拟加载用户数据
        tvUsername.setText("张同学");
        tvStudyDays.setText("已学习 25 天");
        tvTotalQuestions.setText("1,580");
        tvAnsweredQuestions.setText("1,420");
        tvAccuracyRate.setText("82%");
    }
}