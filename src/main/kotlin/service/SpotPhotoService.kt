package com.sportsocial.service

import com.sportsocial.model.Spot
import com.sportsocial.model.SpotPhoto
import com.sportsocial.repository.SpotPhotoRepository
import com.sportsocial.repository.SpotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SpotPhotoService(
    private val spotPhotoRepository: SpotPhotoRepository,
    private val spotRepository: SpotRepository
) {
    fun getBySpotId(spotId: Long): List<SpotPhoto> = spotPhotoRepository.findBySpotId(spotId)
    
    fun getMainPhotoBySpotId(spotId: Long): SpotPhoto? = spotPhotoRepository.findBySpotIdAndIsMainTrue(spotId)
    
    @Transactional
    fun addPhoto(spotId: Long, photoUrl: String, isMain: Boolean = false): SpotPhoto? {
        val spot = spotRepository.findById(spotId).orElse(null) ?: return null
        
        // Если это главное фото, сбрасываем флаг у других фото
        if (isMain) {
            spotPhotoRepository.clearMainPhotoForSpot(spotId)
        }
        
        val photo = SpotPhoto(
            spot = spot,
            photoUrl = photoUrl,
            isMain = isMain
        )
        
        return spotPhotoRepository.save(photo)
    }
    
    @Transactional
    fun setMainPhoto(spotId: Long, photoId: Long): Boolean {
        val photo = spotPhotoRepository.findById(photoId).orElse(null) ?: return false
        if (photo.spot.id != spotId) return false
        
        // Сбрасываем флаг у всех фото спота
        spotPhotoRepository.clearMainPhotoForSpot(spotId)
        
        // Устанавливаем флаг для выбранного фото
        val updatedPhoto = photo.copy(isMain = true)
        spotPhotoRepository.save(updatedPhoto)
        
        return true
    }
    
    fun deletePhoto(spotId: Long, photoId: Long): Boolean {
        val photo = spotPhotoRepository.findById(photoId).orElse(null) ?: return false
        if (photo.spot.id != spotId) return false
        
        spotPhotoRepository.delete(photo)
        return true
    }
} 