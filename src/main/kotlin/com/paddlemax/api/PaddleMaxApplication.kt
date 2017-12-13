package com.paddlemax.api

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource


@SpringBootApplication
@EnableAutoConfiguration(
    exclude = arrayOf(
        JacksonAutoConfiguration::class))
@PropertySource("classpath:\${envTarget:dev}.yml")
class PaddleMaxApplication

fun main(args: Array<String>) {
    SpringApplication.run(PaddleMaxApplication::class.java, *args)
}
