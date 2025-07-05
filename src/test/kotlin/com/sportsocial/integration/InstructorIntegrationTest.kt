package com.sportsocial.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.sportsocial.dto.InstructorDto
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
class InstructorIntegrationTest {

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
    fun `GET api instructors with valid id should return instructor`() {
        mockMvc.perform(get("/api/instructors/1"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.specialization").exists())
            .andExpect(jsonPath("$.experience").exists())
            .andExpect(jsonPath("$.price").exists())
    }

    @Test
    fun `GET api instructors with invalid id should return null`() {
        mockMvc.perform(get("/api/instructors/999"))
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    fun `POST api instructors with valid data should create instructor`() {
        val instructorDto = InstructorDto(
            id = 0,
            name = "Test Instructor",
            specialization = "Test Specialization",
            experience = 5,
            description = "Test Description",
            price = 1000.0,
            contacts = "+79998887766"
        )

        mockMvc.perform(
            post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instructorDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test Instructor"))
            .andExpect(jsonPath("$.specialization").value("Test Specialization"))
            .andExpect(jsonPath("$.experience").value(5))
            .andExpect(jsonPath("$.price").value(1000.0))
    }

    @Test
    fun `POST api instructors with missing required fields should return 400`() {
        val invalidDto = mapOf(
            "name" to "Test Instructor",
            "price" to 1000.0
            // Missing required fields
        )

        mockMvc.perform(
            post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST api instructors with invalid validation should return 400`() {
        val instructorDto = InstructorDto(
            id = 0,
            name = "", // Invalid: empty name
            specialization = "Test Specialization",
            experience = -1, // Invalid: negative experience
            description = "Test Description",
            price = -100.0, // Invalid: negative price
            contacts = "" // Invalid: empty contacts
        )

        mockMvc.perform(
            post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instructorDto))
        )
            .andExpect(status().isBadRequest)
    }
} 