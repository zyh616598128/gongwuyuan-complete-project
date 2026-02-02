package com.gwy.service;

import com.gwy.model.Question;
import com.gwy.model.Subject;
import com.gwy.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Page<Question> getQuestionsBySubject(Subject subject, Pageable pageable) {
        return questionRepository.findBySubjectAndIsActiveTrue(subject, pageable);
    }

    public List<Question> getQuestionsBySubject(Subject subject) {
        return questionRepository.findBySubjectAndIsActiveTrue(subject);
    }

    public Page<Question> getQuestionsBySubjectId(Long subjectId, Pageable pageable) {
        return questionRepository.findBySubject_IdAndIsActiveTrue(subjectId, pageable);
    }

    public Page<Question> getQuestionsByCategoryId(Long categoryId, Pageable pageable) {
        return questionRepository.findByCategory_IdAndIsActiveTrue(categoryId, pageable);
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findByIdIn(List<Long> ids) {
        return questionRepository.findByIdIn(ids);
    }

    public Question save(Question question) {
        if (question.getId() == null) {
            question.setCreateTime(LocalDateTime.now());
        }
        question.setUpdateTime(LocalDateTime.now());
        return questionRepository.save(question);
    }

    public void deleteById(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question != null) {
            question.setIsActive(false);
            questionRepository.save(question);
        }
    }
}