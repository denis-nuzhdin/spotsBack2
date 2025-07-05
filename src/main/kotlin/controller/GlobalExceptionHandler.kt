package com.sportsocial.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import jakarta.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage ?: "Validation error"
        }
        return ResponseEntity.badRequest().body(errors)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        ex.constraintViolations.forEach { violation ->
            errors[violation.propertyPath.toString()] = violation.message
        }
        return ResponseEntity.badRequest().body(errors)
    }
} 