package com.paddlemax.api.config.oauth

//package com.paddlemax.api.config
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
//import javax.annotation.Resource
//
//
//@Configuration
//@EnableResourceServer
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//internal class ResourceServerConfig: ResourceServerConfigurerAdapter() {
//
//    @Autowired
//    private lateinit var authServerConfig: AuthServerConfig
//
//    override fun configure(http: HttpSecurity) {
//        http.sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//            .and()
//            .authorizeRequests()
//            .anyRequest()
//            .permitAll()
//    }
//
//    override fun configure(config: ResourceServerSecurityConfigurer) {
//        config.tokenServices(authServerConfig.tokenServices())
//    }
//}
