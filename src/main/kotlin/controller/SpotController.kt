package com.sportsocial.controller

import com.sportsocial.dto.*
import com.sportsocial.model.Spot
import com.sportsocial.model.SpotType
import com.sportsocial.service.SpotService
import com.sportsocial.service.SpotTypeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/spots")
@Tag(name = "Spots", description = "API для работы со спотами")
class SpotController(
    private val spotService: SpotService,
    private val spotTypeService: SpotTypeService
) {
    
    @GetMapping
    @Operation(summary = "Получить все споты", description = "Возвращает список всех спотов с возможностью фильтрации")
    fun getAll(
        @Parameter(description = "Поисковый запрос (по названию или городу)", required = false)
        @RequestParam(required = false) query: String?,
        @Parameter(description = "ID типа спота для фильтрации", required = false)
        @RequestParam(required = false) spotTypeId: Long?
    ): List<SpotResponseDto> {
        if (spotTypeId != null) {
            val spotType = spotTypeService.getById(spotTypeId)
            if (spotType == null) {
                return emptyList()
            }
        }
        
        val spots = if (query.isNullOrBlank() && spotTypeId == null) {
            spotService.getAll()
        } else {
            spotService.findBySpotTypeAndQuery(spotTypeId, query ?: "")
        }
        return spots.map { it.toResponseDto() }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить спот по ID", description = "Возвращает спот по указанному ID")
    fun getById(
        @Parameter(description = "ID спота", required = true)
        @PathVariable id: Long
    ): ResponseEntity<SpotResponseDto> {
        val spot = spotService.getById(id)
        return if (spot != null) {
            ResponseEntity.ok(spot.toResponseDto())
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @PostMapping
    @Operation(summary = "Создать новый спот", description = "Создает новый спот. spotTypeId обязателен.")
    fun create(@Valid @RequestBody dto: SpotCreateDto): ResponseEntity<SpotResponseDto> {
        val spotType = spotTypeService.getById(dto.spotTypeId)
            ?: return ResponseEntity.badRequest().build()
        
        val spot = Spot(
            name = dto.name,
            description = dto.description,
            city = dto.city,
            address = dto.address,
            price = dto.price,
            schedule = dto.schedule,
            latitude = dto.latitude,
            longitude = dto.longitude,
            mainPhotoUrl = dto.mainPhotoUrl,
            spotType = spotType
        )
        
        return try {
            val createdSpot = spotService.create(spot)
            ResponseEntity.ok(createdSpot.toResponseDto())
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить спот", description = "Обновляет существующий спот. spotTypeId обязателен.")
    fun update(
        @Parameter(description = "ID спота", required = true)
        @PathVariable id: Long, 
        @Valid @RequestBody dto: SpotUpdateDto
    ): ResponseEntity<SpotResponseDto> {
        val existing = spotService.getById(id) ?: return ResponseEntity.notFound().build()
        
        val spotType = if (dto.spotTypeId != existing.spotType.id) {
            spotTypeService.getById(dto.spotTypeId) ?: return ResponseEntity.badRequest().build()
        } else {
            existing.spotType
        }
        
        val updatedSpot = existing.copy(
            name = dto.name ?: existing.name,
            description = dto.description ?: existing.description,
            city = dto.city ?: existing.city,
            address = dto.address ?: existing.address,
            price = dto.price ?: existing.price,
            schedule = dto.schedule ?: existing.schedule,
            latitude = dto.latitude ?: existing.latitude,
            longitude = dto.longitude ?: existing.longitude,
            mainPhotoUrl = dto.mainPhotoUrl ?: existing.mainPhotoUrl,
            spotType = spotType
        )
        
        return try {
            val updated = spotService.update(id, updatedSpot)
            if (updated != null) {
                ResponseEntity.ok(updated.toResponseDto())
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить спот", description = "Удаляет спот по указанному ID")
    fun delete(
        @Parameter(description = "ID спота", required = true)
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        return if (spotService.delete(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping("/by-type/{spotTypeId}")
    @Operation(summary = "Получить споты по типу", description = "Возвращает все споты указанного типа")
    fun getBySpotType(
        @Parameter(description = "ID типа спота", required = true)
        @PathVariable spotTypeId: Long
    ): ResponseEntity<List<SpotResponseDto>> {
        val spotType = spotTypeService.getById(spotTypeId)
        return if (spotType != null) {
            val spots = spotService.findBySpotType(spotTypeId)
            ResponseEntity.ok(spots.map { it.toResponseDto() })
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

fun Spot.toResponseDto() = SpotResponseDto(
    id = id,
    name = name,
    description = description,
    city = city,
    address = address,
    price = price,
    schedule = schedule,
    latitude = latitude,
    longitude = longitude,
    spotType = spotType.toDto()?: SpotTypeDto(0, "Unknown", null),
    mainPhotoUrl = mainPhotoUrl,
    photos = photos.map { it.toDto() }
) 