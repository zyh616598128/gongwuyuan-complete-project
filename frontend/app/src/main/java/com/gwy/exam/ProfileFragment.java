package com.gwy.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvMemberLevel;
    private TextView tvStudyDays;
    private TextView tvTotalQuestions;
    private TextView tvAnsweredQuestions;
    private TextView tvAccuracyRate;
    
    private LinearLayout layoutFavorites;
    private LinearLayout layoutStudyPlan;
    private LinearLayout layoutSettings;
    private LinearLayout layoutHelpFeedback;
    private LinearLayout layoutAbout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        initViews(view);
        setupClickListeners();
        loadUserData();
        
        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvMemberLevel = view.findViewById(R.id.tv_member_level);
        tvStudyDays = view.findViewById(R.id.tv_study_days);
        tvTotalQuestions = view.findViewById(R.id.tv_total_questions);
        tvAnsweredQuestions = view.findViewById(R.id.tv_answered_questions);
        tvAccuracyRate = view.findViewById(R.id.tv_accuracy_rate);
        
        // 功能列表
        layoutFavorites = view.findViewById(R.id.layout_favorites);
        layoutStudyPlan = view.findViewById(R.id.layout_study_plan);
        layoutSettings = view.findViewById(R.id.layout_settings);
        layoutHelpFeedback = view.findViewById(R.id.layout_help_feedback);
        layoutAbout = view.findViewById(R.id.layout_about);
    }

    private void setupClickListeners() {
        // 我的收藏
        layoutFavorites.setOnClickListener(v -> {
            // 跳转到收藏页面
            Intent intent = new Intent(getActivity(), FavoritesActivity.class);
            startActivity(intent);
        });

        // 学习计划
        layoutStudyPlan.setOnClickListener(v -> {
            // 跳转到学习计划页面
            Intent intent = new Intent(getActivity(), StudyPlanActivity.class);
            startActivity(intent);
        });

        // 设置
        layoutSettings.setOnClickListener(v -> {
            // 跳转到设置页面
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        // 帮助与反馈
        layoutHelpFeedback.setOnClickListener(v -> {
            // 跳转到帮助反馈页面
            Intent intent = new Intent(getActivity(), HelpFeedbackActivity.class);
            startActivity(intent);
        });

        // 关于我们
        layoutAbout.setOnClickListener(v -> {
            // 跳转到关于页面
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        // 模拟加载用户数据
        tvUsername.setText("张同学");
        tvMemberLevel.setText("高级会员");
        tvStudyDays.setText("已学习 25 天");
        tvTotalQuestions.setText("1,580");
        tvAnsweredQuestions.setText("1,420");
        tvAccuracyRate.setText("82%");
    }
}