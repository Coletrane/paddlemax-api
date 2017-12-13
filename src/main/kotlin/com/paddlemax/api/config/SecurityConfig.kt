package com.paddlemax.api.config

import com.paddlemax.api.config.AuthEntryPoint
import com.paddlemax.api.config.AuthSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@ComponentScan
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var authEntry: AuthEntryPoint

    @Autowired
    private lateinit var authSuccessHandler: AuthSuccessHandler

//    @Bean
//    public override fun userDetailsService(): UserDetailsService {
//        val manager = InMemoryUserDetailsManager()
//        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build())
//        return manager
//    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(
                "/user/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic()
            .authenticationEntryPoint(authEntry)
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }

    @Bean
    fun mySuccessHandler(): AuthSuccessHandler {
        return AuthSuccessHandler()
    }

    // TODO: implement failure handler
    @Bean
    fun myFailureHandler(): SimpleUrlAuthenticationFailureHandler {
        return SimpleUrlAuthenticationFailureHandler()
    }
}
