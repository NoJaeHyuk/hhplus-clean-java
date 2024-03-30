package com.example.hhpluscleanjava.lecture.infrastructure;

import com.example.hhpluscleanjava.lecture.domain.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select C from Lecture C where C.id = :id")
    Optional<Lecture> findByIdWithPessimisticLock(@Param("id") Long id);
}
