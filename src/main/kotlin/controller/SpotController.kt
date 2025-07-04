package com.sportsocial.controller

import com.sportsocial.dto.SpotDto
import com.sportsocial.model.Spot
import com.sportsocial.service.SpotService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/spots")
class SpotController(private val spotService: SpotService) {
    @GetMapping
    fun getAll(@RequestParam(required = false) query: String?): List<SpotDto> =
        (if (query.isNullOrBlank()) spotService.getAll() else spotService.search(query)).map { it.toDto() }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): SpotDto? = spotService.getById(id)?.toDto()

    @PostMapping
    fun create(@RequestBody dto: SpotDto): SpotDto = spotService.save(dto.toEntity()).toDto()
}

fun Spot.toDto() = SpotDto(id, name, city, address, price, schedule, latitude, longitude)
fun SpotDto.toEntity() = Spot(
    id = this.id ?: 0,
    name = this.name,
    city = this.city,
    address = this.address,
    price = this.price,
    schedule = this.schedule,
    latitude = this.latitude,
    longitude = this.longitude
) 