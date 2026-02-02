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
import com.gwy.exam.adapter.QuestionAdapter;
import com.gwy.exam.model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionBankFragment extends Fragment implements QuestionAdapter.OnQuestionClickListener {

    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_bank, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadQuestionData();
        
        return view;
    }

    private void initViews(View view) {
        recyclerViewQuestions = view.findViewById(R.id.recycler_view_questions);
    }

    private void setupRecyclerView() {
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter();
        questionAdapter.setOnQuestionClickListener(this);
        
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewQuestions.setAdapter(questionAdapter);
    }

    private void loadQuestionData() {
        // 模拟题目数据
        List<String> options1 = new ArrayList<>();
        options1.add("A. 言论自由");
        options1.add("B. 出版自由");
        options1.add("C. 罢工自由");
        options1.add("D. 集会自由");

        List<String> answers1 = new ArrayList<>();
        answers1.add("C");

        questionList.add(new Question(1L, 
                "下列选项中，哪一项不属于我国《宪法》规定的基本权利？", 
                "根据我国《宪法》规定，公民享有言论、出版、集会、结社、游行、示威等自由权利。但并未规定罢工自由。", 
                "single_choice", "medium", options1, answers1, "法律常识", "宪法", 78, true, true, "C", false));

        List<String> options2 = new ArrayList<>();
        options2.add("A. 人民代表大会制度是我国的根本制度");
        options2.add("B. 社会主义制度是中华人民共和国的根本制度");
        options2.add("C. 中国共产党领导是中国特色社会主义最本质的特征");
        options2.add("D. 中华人民共和国的一切权力属于人民");

        List<String> answers2 = new ArrayList<>();
        answers2.add("A");

        questionList.add(new Question(2L, 
                "下列关于我国宪法的说法，错误的是：", 
                "人民代表大会制度是我国的根本政治制度，而不是根本制度。社会主义制度是中华人民共和国的根本制度。", 
                "single_choice", "easy", options2, answers2, "法律常识", "宪法", 85, false, false, null, true));

        List<String> options3 = new ArrayList<>();
        options3.add("A. 国家治理体系和治理能力现代化");
        options3.add("B. 全面深化改革");
        options3.add("C. 全面依法治国");
        options3.add("D. 全面从严治党");

        List<String> answers3 = new ArrayList<>();
        answers3.add("A");

        questionList.add(new Question(3L, 
                "“四个全面”战略布局中，具有突破性和先导性的关键环节是：", 
                "全面深化改革是“四个全面”战略布局中具有突破性和先导性的关键环节。", 
                "single_choice", "hard", options3, answers3, "政治常识", "时政", 65, true, false, "C", false));

        questionAdapter.setQuestions(questionList);
    }

    @Override
    public void onQuestionClick(Question question) {
        // 处理题目点击事件
        // 可以跳转到题目详情页面
    }

    @Override
    public void onDoNowClick(Question question) {
        // 处理“立即练习”点击事件
        // 可以跳转到练习页面
    }
}