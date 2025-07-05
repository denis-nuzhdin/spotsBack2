package com.sportsocial.repository

import com.sportsocial.model.SpotPhoto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface SpotPhotoRepository : JpaRepository<SpotPhoto, Long> {
    fun findBySpotId(spotId: Long): List<SpotPhoto>
    fun findBySpotIdAndIsMainTrue(spotId: Long): SpotPhoto?
    
    @Modifying
    @Query("UPDATE SpotPhoto sp SET sp.isMain = false WHERE sp.spot.id = :spotId")
    fun clearMainPhotoForSpot(spotId: Long)
} 