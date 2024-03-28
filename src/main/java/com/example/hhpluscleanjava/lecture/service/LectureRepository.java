package com.example.hhpluscleanjava.lecture.service;

import com.example.hhpluscleanjava.lecture.domain.Lecture;

import java.util.Optional;

public interface LectureRepository {
    Optional<Lecture> findById(Long lectureId);
}
