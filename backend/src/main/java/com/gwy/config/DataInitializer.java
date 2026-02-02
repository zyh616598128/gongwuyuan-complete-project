package com.gwy.config;

import com.gwy.model.Subject;
import com.gwy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化默认科目
        initializeSubjects();
    }

    private void initializeSubjects() {
        List<String> defaultSubjects = Arrays.asList(
            "行政职业能力测验",
            "申论",
            "公共基础知识",
            "面试技巧"
        );

        for (String subjectName : defaultSubjects) {
            if (subjectRepository.findByName(subjectName).isEmpty()) {
                Subject subject = new Subject();
                subject.setName(subjectName);
                subject.setDescription("公务员考试科目 - " + subjectName);
                subjectRepository.save(subject);
            }
        }
    }
}