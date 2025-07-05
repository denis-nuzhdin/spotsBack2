package com.sportsocial.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.sportsocial.dto.SpotCreateDto
import com.sportsocial.dto.SpotUpdateDto
import com.sportsocial.dto.SpotTypeDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class SpotIntegrationTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `GET api spots should return all spots`() {
        mockMvc.perform(get("/api/spots"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].spotType").exists())
    }

    @Test
    fun `GET api spots with query parameter should return filtered spots`() {
        mockMvc.perform(get("/api/spots").param("query", "Москва"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET api spots with spotTypeId parameter should return spots by type`() {
        mockMvc.perform(get("/api/spots").param("spotTypeId", "1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET api spots with invalid spotTypeId should return empty array`() {
        mockMvc.perform(get("/api/spots").param("spotTypeId", "999"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `GET api spots with valid id should return spot`() {
        mockMvc.perform(get("/api/spots/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.spotType").exists())
    }

    @Test
    fun `GET api spots with invalid id should return 404`() {
        mockMvc.perform(get("/api/spots/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api spots with valid data should create spot`() {
        val spotCreateDto = SpotCreateDto(
            name = "Test Spot",
            description = "Test Description",
            city = "Test City",
            address = "Test Address",
            price = 100.0,
            schedule = "Mon-Fri 9-18",
            latitude = 55.7558,
            longitude = 37.6176,
            spotTypeId = 1,
            mainPhotoUrl = "https://example.com/photo.jpg"
        )

        mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotCreateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Spot"))
            .andExpect(jsonPath("$.spotType.id").value(1))
    }

    @Test
    fun `POST api spots with invalid spotTypeId should return 400`() {
        val spotCreateDto = SpotCreateDto(
            name = "Test Spot",
            description = "Test Description",
            city = "Test City",
            address = "Test Address",
            price = 100.0,
            schedule = "Mon-Fri 9-18",
            latitude = 55.7558,
            longitude = 37.6176,
            spotTypeId = 999,
            mainPhotoUrl = "https://example.com/photo.jpg"
        )

        mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotCreateDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST api spots with missing required fields should return 400`() {
        val invalidDto = mapOf(
            "name" to "Test Spot",
            "price" to 100.0
            // Missing required fields
        )

        mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST api spots with invalid validation should return 400`() {
        val spotCreateDto = SpotCreateDto(
            name = "", // Invalid: empty name
            description = "Test Description",
            city = "Test City",
            address = "Test Address",
            price = -100.0, // Invalid: negative price
            schedule = "Mon-Fri 9-18",
            latitude = 55.7558,
            longitude = 37.6176,
            spotTypeId = 0, // Invalid: zero spotTypeId
            mainPhotoUrl = "https://example.com/photo.jpg"
        )

        mockMvc.perform(
            post("/api/spots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotCreateDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PUT api spots with valid data should update spot`() {
        val spotUpdateDto = SpotUpdateDto(
            name = "Updated Spot",
            price = 150.0,
            spotTypeId = 1
        )

        mockMvc.perform(
            put("/api/spots/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotUpdateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Spot"))
            .andExpect(jsonPath("$.price").value(150.0))
    }

    @Test
    fun `PUT api spots with invalid id should return 404`() {
        val spotUpdateDto = SpotUpdateDto(
            name = "Updated Spot",
            spotTypeId = 1
        )

        mockMvc.perform(
            put("/api/spots/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotUpdateDto))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT api spots with invalid spotTypeId should return 400`() {
        val spotUpdateDto = SpotUpdateDto(
            name = "Updated Spot",
            spotTypeId = 999
        )

        mockMvc.perform(
            put("/api/spots/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotUpdateDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PUT api spots with invalid validation should return 400`() {
        val spotUpdateDto = SpotUpdateDto(
            name = "", // Invalid: empty name
            price = -50.0, // Invalid: negative price
            spotTypeId = 0 // Invalid: zero spotTypeId
        )

        mockMvc.perform(
            put("/api/spots/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotUpdateDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `DELETE api spots with valid id should delete spot`() {
        mockMvc.perform(delete("/api/spots/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `DELETE api spots with invalid id should return 404`() {
        mockMvc.perform(delete("/api/spots/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET api spots by type with valid type should return spots`() {
        mockMvc.perform(get("/api/spots/by-type/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET api spots by type with invalid type should return 404`() {
        mockMvc.perform(get("/api/spots/by-type/999"))
            .andExpect(status().isNotFound)
    }
} 