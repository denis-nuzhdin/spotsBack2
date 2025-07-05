package com.sportsocial.integration

import com.fasterxml.jackson.databind.ObjectMapper
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
class SpotPhotoIntegrationTest {

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
    fun `GET api spots photos should return all photos for spot`() {
        mockMvc.perform(get("/api/spots/1/photos"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `GET api spots photos with invalid spot id should return empty array`() {
        mockMvc.perform(get("/api/spots/999/photos"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `GET api spots photos main should return main photo`() {
        mockMvc.perform(get("/api/spots/1/photos/main"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.photoUrl").exists())
            .andExpect(jsonPath("$.main").value(true))
    }

    @Test
    fun `GET api spots photos main with invalid spot id should return 404`() {
        mockMvc.perform(get("/api/spots/999/photos/main"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api spots photos with valid data should add photo`() {
        val addPhotoRequest = mapOf(
            "photoUrl" to "https://example.com/new-photo.jpg",
            "isMain" to false
        )

        mockMvc.perform(
            post("/api/spots/1/photos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addPhotoRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.photoUrl").value("https://example.com/new-photo.jpg"))
            .andExpect(jsonPath("$.main").value(false))
    }

    @Test
    fun `POST api spots photos with invalid spot id should return 404`() {
        val addPhotoRequest = mapOf(
            "photoUrl" to "https://example.com/new-photo.jpg",
            "isMain" to false
        )

        mockMvc.perform(
            post("/api/spots/999/photos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addPhotoRequest))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST api spots photos with missing photoUrl should return 400`() {
        val invalidRequest = mapOf(
            "isMain" to false
            // Missing photoUrl
        )

        mockMvc.perform(
            post("/api/spots/1/photos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST api spots photos with empty photoUrl should return 400`() {
        val invalidRequest = mapOf(
            "photoUrl" to "",
            "isMain" to false
        )

        mockMvc.perform(
            post("/api/spots/1/photos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `PUT api spots photos main should set photo as main`() {
        mockMvc.perform(put("/api/spots/1/photos/1/main"))
            .andExpect(status().isOk)
    }

    @Test
    fun `PUT api spots photos main with invalid spot id should return 404`() {
        mockMvc.perform(put("/api/spots/999/photos/1/main"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT api spots photos main with invalid photo id should return 404`() {
        mockMvc.perform(put("/api/spots/1/photos/999/main"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `DELETE api spots photos with valid ids should delete photo`() {
        mockMvc.perform(delete("/api/spots/1/photos/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `DELETE api spots photos with invalid spot id should return 404`() {
        mockMvc.perform(delete("/api/spots/999/photos/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `DELETE api spots photos with invalid photo id should return 404`() {
        mockMvc.perform(delete("/api/spots/1/photos/999"))
            .andExpect(status().isNotFound)
    }
} 