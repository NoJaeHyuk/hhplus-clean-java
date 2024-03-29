package com.example.hhpluscleanjava.lecture.infrastructure;

import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantJpaRepository extends JpaRepository<Applicant, Long> {

    boolean existsByLectureAndUserId(Lecture lecture, Long userId);

    Optional<Applicant> findByLectureIdAndUserId(Long lectureId, Long userId);
}
