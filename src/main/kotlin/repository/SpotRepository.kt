package com.sportsocial.repository

import com.sportsocial.model.Spot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SpotRepository : JpaRepository<Spot, Long> {
    
    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN FETCH s.spotType LEFT JOIN FETCH s.photos")
    fun findAllWithSpotTypeAndPhotos(): List<Spot>
    
    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN FETCH s.spotType LEFT JOIN FETCH s.photos WHERE s.id = :id")
    fun findByIdWithSpotTypeAndPhotos(@Param("id") id: Long): Spot?
    
    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN FETCH s.spotType LEFT JOIN FETCH s.photos WHERE s.city LIKE %:city% OR s.name LIKE %:name%")
    fun findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(@Param("city") city: String, @Param("name") name: String): List<Spot>
    
    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN FETCH s.spotType LEFT JOIN FETCH s.photos WHERE s.spotType.id = :spotTypeId")
    fun findBySpotTypeId(@Param("spotTypeId") spotTypeId: Long): List<Spot>
    
    @Query("SELECT DISTINCT s FROM Spot s LEFT JOIN FETCH s.spotType LEFT JOIN FETCH s.photos WHERE (:spotTypeId IS NULL OR s.spotType.id = :spotTypeId) AND (s.city LIKE %:query% OR s.name LIKE %:query%)")
    fun findBySpotTypeAndQuery(@Param("spotTypeId") spotTypeId: Long?, @Param("query") query: String): List<Spot>
} 