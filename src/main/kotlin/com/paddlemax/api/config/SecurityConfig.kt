import com.paddlemax.api.config.AuthEntryPoint
import com.paddlemax.api.config.AuthSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.bouncycastle.cms.RecipientId.password
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.core.userdetails.UserDetailsService

@Configuration
@EnableWebSecurity
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
        auth.jdbcAuthentication()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authEntry)
            .and()
            .authorizeRequests()
            .antMatchers("/**").authenticated()
            .and()
            .formLogin()
            .successHandler(authSuccessHandler)
            .failureHandler(SimpleUrlAuthenticationFailureHandler())
            .and()
            .logout()
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
