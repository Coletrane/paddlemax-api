package com.paddlemax.api.config

import com.auth0.spring.security.api.JwtWebSecurityConfigurer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Value("\${auth0.apiAudience}")
    private lateinit var apiAudience: String

    @Value("\${auth0.issuer}")
    private lateinit var issuer: String

    override fun configure(http: HttpSecurity) {
//        @formatter:off
        JwtWebSecurityConfigurer
            .forRS256(apiAudience, issuer)
            .configure(http)
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "v1/user/register")
                .permitAll()
            .antMatchers(HttpMethod.GET, "v1/user/me")
                .hasAuthority("read:user")
            .antMatchers(HttpMethod.PUT, "v1/user")
                .hasAuthority("update:user")
//        @formatter:on
    }
}
