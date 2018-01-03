package com.paddlemax.api.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.nio.file.FileSystem

@Configuration
@ComponentScan
class PropertySourceConfig {

    companion object {

        val log = LoggerFactory.getLogger(PropertySourceConfig::class.java)

        val prodLocation = "prod.yml"
        val prodServerLocation = "/srv/max/prod.yml"
        val devLocation = "dev.yml"

        @Bean
        fun properties(): PropertySourcesPlaceholderConfigurer {
            val ppc = PropertySourcesPlaceholderConfigurer()

            val prod = ClassPathResource(prodLocation)
            if (prod.exists()) {
                ppc.setLocation(prod)
            } else {
                log.warn("No config found in ${prodLocation}")
            }

            val prodServer = FileSystemResource(prodServerLocation)
            if (prodServer.exists()) {
                ppc.setLocation(prodServer)
            } else {
                log.warn("No config found in ${prodServerLocation}")
            }

            val dev = ClassPathResource(devLocation)
            if (dev.exists()) {
                ppc.setLocation(dev)
            } else {
                log.warn("No config found in ${devLocation}")
            }

            return ppc
        }
    }
}
