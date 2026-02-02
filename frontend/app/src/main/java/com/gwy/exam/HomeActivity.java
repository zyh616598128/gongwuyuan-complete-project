package com.gwy.exam;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gwy.exam.HomeFragment;
import com.gwy.exam.ui.practice.PracticeFragment;
import com.gwy.exam.ui.exam.ExamFragment;
import com.gwy.exam.ui.question.WrongQuestionsFragment;
import com.gwy.exam.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // 默认加载首页
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_practice:
                            selectedFragment = new PracticeFragment();
                            break;
                        case R.id.nav_exam:
                            selectedFragment = new ExamFragment();
                            break;
                        case R.id.nav_wrong_questions:
                            selectedFragment = new WrongQuestionsFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            };
}