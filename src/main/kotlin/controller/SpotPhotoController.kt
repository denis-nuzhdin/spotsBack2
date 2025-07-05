package com.sportsocial.controller

import com.sportsocial.dto.SpotPhotoDto
import com.sportsocial.service.SpotPhotoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/spots/{spotId}/photos")
class SpotPhotoController(private val spotPhotoService: SpotPhotoService) {
    
    @GetMapping
    fun getPhotos(@PathVariable spotId: Long): List<SpotPhotoDto> = 
        spotPhotoService.getBySpotId(spotId).map { it.toDto() }
    
    @GetMapping("/main")
    fun getMainPhoto(@PathVariable spotId: Long): ResponseEntity<SpotPhotoDto> {
        val photo = spotPhotoService.getMainPhotoBySpotId(spotId)
        return if (photo != null) {
            ResponseEntity.ok(photo.toDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    fun addPhoto(
        @PathVariable spotId: Long,
        @RequestBody @Valid request: AddPhotoRequest
    ): ResponseEntity<SpotPhotoDto> {
        val photo = spotPhotoService.addPhoto(spotId, request.photoUrl, request.isMain)
        return if (photo != null) {
            ResponseEntity.ok(photo.toDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PutMapping("/{photoId}/main")
    fun setMainPhoto(@PathVariable spotId: Long, @PathVariable photoId: Long): ResponseEntity<Unit> {
        return if (spotPhotoService.setMainPhoto(spotId, photoId)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @DeleteMapping("/{photoId}")
    fun deletePhoto(@PathVariable spotId: Long, @PathVariable photoId: Long): ResponseEntity<Unit> {
        return if (spotPhotoService.deletePhoto(spotId, photoId)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class AddPhotoRequest(
    @field:NotBlank(message = "Photo URL is required")
    val photoUrl: String,
    val isMain: Boolean = false
)

internal fun com.sportsocial.model.SpotPhoto.toDto() = SpotPhotoDto(id, photoUrl, isMain, createdAt) 