package com.sportsocial.service

import com.sportsocial.model.Instructor
import com.sportsocial.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(private val instructorRepository: InstructorRepository) {
    fun getById(id: Long): Instructor? = instructorRepository.findById(id).orElse(null)
    fun create(instructor: Instructor): Instructor = instructorRepository.save(instructor)
} 