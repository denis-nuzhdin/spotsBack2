package com.sportsocial.service

import com.sportsocial.model.Spot
import com.sportsocial.repository.SpotRepository
import com.sportsocial.repository.SpotTypeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpotService(
    private val spotRepository: SpotRepository,
    private val spotTypeRepository: SpotTypeRepository
) {
    fun getAll(): List<Spot> = spotRepository.findAllWithSpotTypeAndPhotos().filter { it.spotType != null }
    
    fun getById(id: Long): Spot? = spotRepository.findByIdWithSpotTypeAndPhotos(id)?.let { if (it.spotType != null) it else null }
    
    fun search(query: String): List<Spot> =
        spotRepository.findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(query, query).filter { it.spotType != null }
    
    fun findBySpotType(spotTypeId: Long): List<Spot> = spotRepository.findBySpotTypeId(spotTypeId).filter { it.spotType != null }
    
    fun findBySpotTypeAndQuery(spotTypeId: Long?, query: String): List<Spot> {
        return if (query.isBlank()) {
            if (spotTypeId != null) findBySpotType(spotTypeId) else getAll()
        } else {
            spotRepository.findBySpotTypeAndQuery(spotTypeId, query).filter { it.spotType != null }
        }
    }
    
    @Transactional
    fun create(spot: Spot): Spot {
        // Проверяем, что тип спота существует
        val spotType = spotTypeRepository.findById(spot.spotType.id).orElse(null)
            ?: throw IllegalArgumentException("Spot type with id ${spot.spotType.id} not found")
        
        // Проверяем, что название спота не пустое
        if (spot.name.isBlank()) {
            throw IllegalArgumentException("Spot name cannot be empty")
        }
        
        // Проверяем, что описание не пустое
        if (spot.description.isBlank()) {
            throw IllegalArgumentException("Spot description cannot be empty")
        }
        
        return spotRepository.save(spot.copy(spotType = spotType))
    }
    
    @Transactional
    fun update(id: Long, spot: Spot): Spot? {
        val existing = spotRepository.findByIdWithSpotTypeAndPhotos(id) ?: return null
        
        // Проверяем, что тип спота существует (если изменился)
        val spotType = if (spot.spotType.id != existing.spotType.id) {
            spotTypeRepository.findById(spot.spotType.id).orElse(null)
                ?: throw IllegalArgumentException("Spot type with id ${spot.spotType.id} not found")
        } else {
            existing.spotType
        }
        
        // Проверяем, что название спота не пустое
        if (spot.name.isBlank()) {
            throw IllegalArgumentException("Spot name cannot be empty")
        }
        
        // Проверяем, что описание не пустое
        if (spot.description.isBlank()) {
            throw IllegalArgumentException("Spot description cannot be empty")
        }
        
        val updated = existing.copy(
            name = spot.name,
            description = spot.description,
            city = spot.city,
            address = spot.address,
            price = spot.price,
            schedule = spot.schedule,
            latitude = spot.latitude,
            longitude = spot.longitude,
            mainPhotoUrl = spot.mainPhotoUrl,
            spotType = spotType
        )
        
        return spotRepository.save(updated)
    }
    
    fun delete(id: Long): Boolean {
        return if (spotRepository.existsById(id)) {
            spotRepository.deleteById(id)
            true
        } else false
    }
} 