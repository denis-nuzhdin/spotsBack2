package com.sportsocial.service

import com.sportsocial.model.Spot
import com.sportsocial.repository.SpotRepository
import org.springframework.stereotype.Service

@Service
class SpotService(private val spotRepository: SpotRepository) {
    fun getAll(): List<Spot> = spotRepository.findAll()
    fun getById(id: Long): Spot? = spotRepository.findById(id).orElse(null)
    fun search(query: String): List<Spot> =
        spotRepository.findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(query, query)
    fun save(spot: Spot): Spot = spotRepository.save(spot)
} 