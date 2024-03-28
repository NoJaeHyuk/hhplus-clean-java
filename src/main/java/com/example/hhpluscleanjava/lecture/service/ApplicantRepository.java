package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.domain.Applicant;
import com.example.hhpluscleanjava.lecture.domain.Lecture;

public interface ApplicantRepository {

    boolean existsByLectureAndUserId(Lecture lecture, Long userId);

    Applicant save(Applicant applicant);
}
