package com.gwy.repository;

import com.gwy.model.Question;
import com.gwy.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findBySubjectAndIsActiveTrue(Subject subject, Pageable pageable);
    
    List<Question> findBySubjectAndIsActiveTrue(Subject subject);
    
    Page<Question> findBySubject_IdAndIsActiveTrue(Long subjectId, Pageable pageable);
    
    Page<Question> findByCategory_IdAndIsActiveTrue(Long categoryId, Pageable pageable);
    
    List<Question> findByIdIn(List<Long> ids);
    
    List<Question> findByParentQuestionIdAndIsActiveTrue(Long parentQuestionId);
}