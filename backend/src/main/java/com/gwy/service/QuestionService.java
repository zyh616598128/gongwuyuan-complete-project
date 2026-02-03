package com.gwy.service;

import com.gwy.model.Question;
import com.gwy.model.Subject;
import com.gwy.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Cacheable(value = "questions", key = "#subject.id + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Question> getQuestionsBySubject(Subject subject, Pageable pageable) {
        return questionRepository.findBySubjectAndIsActiveTrue(subject, pageable);
    }

    @Cacheable(value = "questions", key = "#subject.id")
    public List<Question> getQuestionsBySubject(Subject subject) {
        return questionRepository.findBySubjectAndIsActiveTrue(subject);
    }

    @Cacheable(value = "questions", key = "#subjectId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Question> getQuestionsBySubjectId(Long subjectId, Pageable pageable) {
        return questionRepository.findBySubject_IdAndIsActiveTrue(subjectId, pageable);
    }

    @Cacheable(value = "questions", key = "#categoryId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Question> getQuestionsByCategoryId(Long categoryId, Pageable pageable) {
        return questionRepository.findByCategory_IdAndIsActiveTrue(categoryId, pageable);
    }

    @Cacheable(value = "questions", key = "#id")
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    @Cacheable(value = "questions", key = "{#ids}")
    public List<Question> findByIdIn(List<Long> ids) {
        return questionRepository.findByIdIn(ids);
    }

    @CacheEvict(value = {"questions", "subjects"}, allEntries = true)
    public Question save(Question question) {
        if (question.getId() == null) {
            question.setCreateTime(LocalDateTime.now());
        }
        question.setUpdateTime(LocalDateTime.now());
        return questionRepository.save(question);
    }

    @CacheEvict(value = {"questions", "subjects"}, allEntries = true)
    public void deleteById(Long id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question != null) {
            question.setIsActive(false);
            questionRepository.save(question);
        }
    }

    @CacheEvict(value = {"questions", "subjects"}, allEntries = true)
    public List<Question> saveAll(List<Question> questions) {
        for (Question question : questions) {
            if (question.getId() == null) {
                question.setCreateTime(LocalDateTime.now());
            }
            question.setUpdateTime(LocalDateTime.now());
        }
        return questionRepository.saveAll(questions);
    }

    public List<Question> getChildrenByParentId(Long parentId) {
        return questionRepository.findByParentQuestionIdAndIsActiveTrue(parentId);
    }
}