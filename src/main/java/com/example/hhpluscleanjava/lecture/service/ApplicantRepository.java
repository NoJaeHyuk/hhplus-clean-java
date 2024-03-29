package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;

import java.util.Optional;

public interface ApplicantRepository {

    boolean existsByLectureAndUserId(Lecture lecture, Long userId);

    Applicant save(Applicant applicant);

    Optional<Applicant> findByLectureIdAndUserId(Long lectureId, Long userId);
}
