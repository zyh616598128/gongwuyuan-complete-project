package com.gwy.service;

import com.gwy.model.Subject;
import com.gwy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Cacheable(value = "subjects", key = "'all'")
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Cacheable(value = "subjects", key = "#id")
    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }
}