package com.kizza.helloworld.service;

import com.kizza.helloworld.student.Student;
import com.kizza.helloworld.student.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> findAll() { return this.studentRepository.findAll(); }

    public Student findById(UUID id) {
        return this.studentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No Student found with id: " + id)
        );
    }

    public Student updateOrInsert(Student student) {
        return this.studentRepository.save(student);
    }

    public Student updateOrInsert(Student student, UUID id) {
        student.setId(id);
        // .save() also updates the entity if the id is set
        return this.studentRepository.save(student);
    }

    public void deleteById(UUID id) { this.studentRepository.deleteById(id); }
}
