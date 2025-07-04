package com.sportsocial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SportSocialApplication

fun main(args: Array<String>) {
    runApplication<SportSocialApplication>(*args)
}