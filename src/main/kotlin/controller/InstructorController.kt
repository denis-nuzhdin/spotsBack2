package com.sportsocial.controller

import com.sportsocial.dto.InstructorDto
import com.sportsocial.model.Instructor
import com.sportsocial.service.InstructorService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/instructors")
class InstructorController(private val instructorService: InstructorService) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): InstructorDto? = instructorService.getById(id)?.toDto()

    @PostMapping
    fun create(@RequestBody dto: InstructorDto): InstructorDto = instructorService.create(dto.toEntity()).toDto()
}

fun Instructor.toDto() = InstructorDto(id, name, specialization, experience, description, price, contacts)
fun InstructorDto.toEntity() = Instructor(
    id = this.id ?: 0,
    name = this.name,
    specialization = this.specialization,
    experience = this.experience,
    description = this.description,
    price = this.price,
    contacts = this.contacts
) 