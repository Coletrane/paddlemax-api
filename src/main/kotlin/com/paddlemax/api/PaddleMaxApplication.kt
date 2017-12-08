package com.paddlemax.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@EnableAutoConfiguration(exclude = arrayOf(
    JacksonAutoConfiguration::class))
@PropertySource(value = arrayOf(
    "file:/Users/coleinman/Programs/PaddleMax/application.properties", // Development environment
    "file:/lib/max/application.properties", // Production environment
    "classpath:/application.properties"),
    ignoreResourceNotFound = true) // Ignore the file for the enviornment we are NOT in
// WARNING: this will fail SILENTLY!!
class PaddleMaxApplication

fun main(args: Array<String>) {
    SpringApplication.run(PaddleMaxApplication::class.java, *args)
}
