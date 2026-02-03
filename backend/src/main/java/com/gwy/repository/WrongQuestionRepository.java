package com.gwy.repository;

import com.gwy.model.WrongQuestion;
import com.gwy.model.Question;
import com.gwy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WrongQuestionRepository extends JpaRepository<WrongQuestion, Long> {
    List<WrongQuestion> findByUserId(Long userId);
    
    Page<WrongQuestion> findByUserId(Long userId, Pageable pageable);
    
    Optional<WrongQuestion> findByUserIdAndQuestionId(Long userId, Long questionId);
    void deleteByUserIdAndQuestionId(Long userId, Long questionId);
    
    @Query("SELECT COUNT(w) FROM WrongQuestion w WHERE w.user.id = :userId")
    int countByUserId(Long userId);
    
    // 按用户和科目查询错题（分页）
    Page<WrongQuestion> findByUserIdAndQuestion_Subject_Id(Long userId, Long subjectId, Pageable pageable);
    
    // 按用户和科目查询错题（非分页）
    List<WrongQuestion> findByUserIdAndQuestion_Subject_Id(Long userId, Long subjectId);
}