package com.paddlemax.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableAutoConfiguration
class PaddleMaxApplication

fun main(args: Array<String>) {
    SpringApplication.run(PaddleMaxApplication::class.java, *args)
}
