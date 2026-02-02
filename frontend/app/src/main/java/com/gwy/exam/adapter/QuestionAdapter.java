package com.gwy.exam.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gwy.exam.R;
import com.gwy.exam.model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questionList;
    private OnQuestionClickListener listener;

    public interface OnQuestionClickListener {
        void onQuestionClick(Question question);
        void onDoNowClick(Question question);
    }

    public QuestionAdapter() {
        this.questionList = new ArrayList<>();
    }

    public void setQuestions(List<Question> questions) {
        this.questionList = questions;
        notifyDataSetChanged();
    }

    public void setOnQuestionClickListener(OnQuestionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber;
        TextView tvQuestionContent;
        TextView tvCorrectRate;
        TextView tvCategory;
        TextView tvAnswerStatus;
        TextView btnDoNow;
        TextView btnViewDetail;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvQuestionContent = itemView.findViewById(R.id.tv_question_content);
            tvCorrectRate = itemView.findViewById(R.id.tv_correct_rate);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvAnswerStatus = itemView.findViewById(R.id.tv_answer_status);
            btnDoNow = itemView.findViewById(R.id.btn_do_now);
            btnViewDetail = itemView.findViewById(R.id.btn_view_detail);

            btnViewDetail.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onQuestionClick(questionList.get(position));
                    }
                }
            });

            btnDoNow.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDoNowClick(questionList.get(position));
                    }
                }
            });
        }

        public void bind(Question question) {
            tvQuestionNumber.setText((getAdapterPosition() + 1) + ".");
            tvQuestionContent.setText(question.getContent());
            tvCorrectRate.setText("正确率: " + question.getCorrectRate() + "%");
            tvCategory.setText(question.getCategory());
            
            // 设置回答状态
            if (question.isAnswered()) {
                tvAnswerStatus.setText("已答");
                tvAnswerStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.success));
            } else {
                tvAnswerStatus.setText("未答");
                tvAnswerStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.warning));
            }
        }
    }
}