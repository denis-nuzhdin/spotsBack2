package com.sportsocial.controller

import com.sportsocial.dto.SpotTypeDto
import com.sportsocial.model.SpotType
import com.sportsocial.service.SpotTypeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/spot-types")
class SpotTypeController(private val spotTypeService: SpotTypeService) {
    
    @GetMapping
    fun getAll(): List<SpotTypeDto> = spotTypeService.getAll().map { it.toDto() }
    
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<SpotTypeDto> {
        val spotType = spotTypeService.getById(id)
        return if (spotType != null) {
            ResponseEntity.ok(spotType.toDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    fun create(@Valid @RequestBody dto: SpotTypeDto): ResponseEntity<SpotTypeDto> {
        return try {
            val spotType = SpotType(
                name = dto.name,
                description = dto.description
            )
            val created = spotTypeService.create(spotType)
            ResponseEntity.ok(created.toDto())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody dto: SpotTypeDto): ResponseEntity<SpotTypeDto> {
        return try {
            val spotType = SpotType(
                name = dto.name,
                description = dto.description
            )
            val updated = spotTypeService.update(id, spotType)
            if (updated != null) {
                ResponseEntity.ok(updated.toDto())
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return if (spotTypeService.delete(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/search")
    fun searchByName(@RequestParam name: String): List<SpotTypeDto> = 
        spotTypeService.searchByName(name).map { it.toDto() }
}

fun SpotType.toDto() = SpotTypeDto(id, name, description) 