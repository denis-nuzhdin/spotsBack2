package com.sportsocial.repository

import com.sportsocial.model.Instructor
import org.springframework.data.jpa.repository.JpaRepository

interface InstructorRepository : JpaRepository<Instructor, Long> {
} 