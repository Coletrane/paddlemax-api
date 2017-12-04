package com.paddlemax.api.db

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@ComponentScan
@PropertySource(value = arrayOf(
     "file:/Users/coleinman/Programs/PaddleMax/datasource.properties", // Development environment
     "file:/srv/max/datasource.properties", // Production environment
     "classpath:/application.properties"),
    ignoreResourceNotFound = true) // Ignore the file for the enviornment we are NOT in
                                   // WARNING: this will fail SILENTLY!!
class PersistenceJPAConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    fun dataSource(): DataSource {
        return DriverManagerDataSource()
    }
}
