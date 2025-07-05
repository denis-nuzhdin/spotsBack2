package com.sportsocial.integration

import com.fasterxml.jackson.databind.ObjectMapper
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
class SpotTypeIntegrationTest {

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
    fun `GET api spot types should return all spot types`() {
        mockMvc.perform(get("/api/spot-types"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].name").exists())
    }

    @Test
    fun `GET api spot types with valid id should return spot type`() {
        mockMvc.perform(get("/api/spot-types/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").exists())
    }

    @Test
    fun `GET api spot types with invalid id should return 404`() {
        mockMvc.perform(get("/api/spot-types/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api spot types with valid data should create spot type`() {
        val spotTypeDto = SpotTypeDto(
            id = 0,
            name = "Test Type",
            description = "Test Description"
        )

        mockMvc.perform(
            post("/api/spot-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotTypeDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Type"))
            .andExpect(jsonPath("$.description").value("Test Description"))
    }

    @Test
    fun `POST api spot types with missing required fields should return 400`() {
        val invalidDto = mapOf(
            "description" to "Test Description"
            // Missing name field
        )

        mockMvc.perform(
            post("/api/spot-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST api spot types with invalid validation should return 400`() {
        val spotTypeDto = SpotTypeDto(
            id = 0,
            name = "", // Invalid: empty name
            description = "Test Description"
        )

        mockMvc.perform(
            post("/api/spot-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotTypeDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PUT api spot types with valid data should update spot type`() {
        val spotTypeDto = SpotTypeDto(
            id = 0,
            name = "Updated Type",
            description = "Updated Description"
        )

        mockMvc.perform(
            put("/api/spot-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotTypeDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Type"))
            .andExpect(jsonPath("$.description").value("Updated Description"))
    }

    @Test
    fun `PUT api spot types with invalid id should return 404`() {
        val spotTypeDto = SpotTypeDto(
            id = 0,
            name = "Updated Type",
            description = "Updated Description"
        )

        mockMvc.perform(
            put("/api/spot-types/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotTypeDto))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT api spot types with invalid validation should return 400`() {
        val spotTypeDto = SpotTypeDto(
            id = 0,
            name = "", // Invalid: empty name
            description = "Updated Description"
        )

        mockMvc.perform(
            put("/api/spot-types/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spotTypeDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `DELETE api spot types with valid id should delete spot type`() {
        mockMvc.perform(delete("/api/spot-types/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `DELETE api spot types with invalid id should return 404`() {
        mockMvc.perform(delete("/api/spot-types/999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET api spot types search with valid name should return filtered types`() {
        mockMvc.perform(get("/api/spot-types/search").param("name", "скейт"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET api spot types search with non existent name should return empty array`() {
        mockMvc.perform(get("/api/spot-types/search").param("name", "nonexistent"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }
} 