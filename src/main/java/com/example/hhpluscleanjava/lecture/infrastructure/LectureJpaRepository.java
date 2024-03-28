package com.example.hhpluscleanjava.lecture.infrastructure;

import com.example.hhpluscleanjava.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
}
