package com.gwy.repository;

import com.gwy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySubjectId(Long subjectId);
    List<Category> findByParentId(Long parentId);
}