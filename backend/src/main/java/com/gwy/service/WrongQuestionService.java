package com.gwy.service;

import com.gwy.model.Question;
import com.gwy.model.User;
import com.gwy.model.WrongQuestion;
import com.gwy.repository.WrongQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WrongQuestionService {

    @Autowired
    private WrongQuestionRepository wrongQuestionRepository;

    public Page<WrongQuestion> findByUserId(Long userId, Pageable pageable) {
        return wrongQuestionRepository.findByUserId(userId, pageable);
    }

    public Page<WrongQuestion> findByUserIdAndSubjectId(Long userId, Long subjectId, Pageable pageable) {
        return wrongQuestionRepository.findByUserIdAndQuestion_Subject_Id(userId, subjectId, pageable);
    }

    public Optional<WrongQuestion> findById(Long id) {
        return wrongQuestionRepository.findById(id);
    }

    public WrongQuestion save(WrongQuestion wrongQuestion) {
        if (wrongQuestion.getId() == null) {
            wrongQuestion.setCreateTime(LocalDateTime.now());
        }
        wrongQuestion.setUpdateTime(LocalDateTime.now());
        return wrongQuestionRepository.save(wrongQuestion);
    }

    public void deleteById(Long id) {
        wrongQuestionRepository.deleteById(id);
    }

    public Optional<WrongQuestion> findByUserIdAndQuestionId(Long userId, Long questionId) {
        return wrongQuestionRepository.findByUserIdAndQuestionId(userId, questionId);
    }
    
    public boolean existsByUserIdAndQuestionId(Long userId, Long questionId) {
        return wrongQuestionRepository.findByUserIdAndQuestionId(userId, questionId).isPresent();
    }

    public List<WrongQuestion> findByUserIdAndSubjectId(Long userId, Long subjectId) {
        return wrongQuestionRepository.findByUserIdAndQuestion_Subject_Id(userId, subjectId);
    }
}