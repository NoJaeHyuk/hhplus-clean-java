package com.example.hhpluscleanjava.lecture.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

    private String title;

    private int capacityCount;

    private ZonedDateTime openDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = true, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, insertable = true, updatable = true)
    private ZonedDateTime updatedAt = null;

    @Builder
    private Lecture(Long id, String title, int capacityCount, ZonedDateTime openDate) {
        this.id = id;
        this.title = title;
        this.capacityCount = capacityCount;
        this.openDate = openDate;
    }

    public boolean isLectureRegistrationOpen(ZonedDateTime currentDate) {
        return this.openDate.isAfter(currentDate);
    }

    public boolean isCapacityExceeded(int maxCount) {
        return this.capacityCount >= maxCount;
    }

    public void addCapacityCount() {
        this.capacityCount += 1;
    }
}
