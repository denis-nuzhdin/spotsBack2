package com.sportsocial.service

import com.sportsocial.model.SpotType
import com.sportsocial.repository.SpotTypeRepository
import org.springframework.stereotype.Service

@Service
class SpotTypeService(private val spotTypeRepository: SpotTypeRepository) {
    fun getAll(): List<SpotType> = spotTypeRepository.findAll()
    
    fun getById(id: Long): SpotType? = spotTypeRepository.findById(id).orElse(null)
    
    fun create(spotType: SpotType): SpotType {
        // Проверяем, что название не пустое
        if (spotType.name.isBlank()) {
            throw IllegalArgumentException("Spot type name cannot be empty")
        }
        
        // Проверяем, что название уникальное (игнорируя регистр)
        val existing = spotTypeRepository.findByNameContainingIgnoreCase(spotType.name)
        if (existing.isNotEmpty()) {
            throw IllegalArgumentException("Spot type with name '${spotType.name}' already exists")
        }
        
        return spotTypeRepository.save(spotType)
    }
    
    fun update(id: Long, spotType: SpotType): SpotType? {
        val existing = spotTypeRepository.findById(id).orElse(null) ?: return null
        
        // Проверяем, что название не пустое
        if (spotType.name.isBlank()) {
            throw IllegalArgumentException("Spot type name cannot be empty")
        }
        
        // Проверяем, что название уникальное (игнорируя регистр), исключая текущий тип
        val existingWithSameName = spotTypeRepository.findByNameContainingIgnoreCase(spotType.name)
        val hasConflict = existingWithSameName.any { it.id != id }
        if (hasConflict) {
            throw IllegalArgumentException("Spot type with name '${spotType.name}' already exists")
        }
        
        val updated = existing.copy(
            name = spotType.name,
            description = spotType.description
        )
        return spotTypeRepository.save(updated)
    }
    
    fun delete(id: Long): Boolean {
        return if (spotTypeRepository.existsById(id)) {
            spotTypeRepository.deleteById(id)
            true
        } else false
    }
    
    fun searchByName(name: String): List<SpotType> = spotTypeRepository.findByNameContainingIgnoreCase(name)
} 