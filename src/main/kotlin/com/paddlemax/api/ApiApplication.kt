package com.paddlemax.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

@SpringBootApplication
// TODO: set up data source
@EnableAutoConfiguration(exclude = arrayOf(DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class))
class ApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(ApiApplication::class.java, *args)
}
