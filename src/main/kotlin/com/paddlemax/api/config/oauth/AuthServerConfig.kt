package com.paddlemax.api.config.oauth

//package com.paddlemax.api.config
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.ApplicationContext
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.context.annotation.PropertySource
//import org.springframework.core.env.Environment
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
//import org.springframework.core.io.ClassPathResource
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client
//import org.springframework.security.oauth2.provider.ClientDetailsService
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices
//import org.springframework.security.oauth2.provider.token.TokenEnhancer
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
//import org.springframework.security.oauth2.provider.token.TokenStore
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
//import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
//import javax.sql.DataSource
//
//
//@Configuration
//@EnableAuthorizationServer
//@EnableOAuth2Client
//class AuthServerConfig: AuthorizationServerConfigurerAdapter() {
//
//    @Autowired
//    private lateinit var env: Environment
//
//    @Autowired
//    private lateinit var context: ApplicationContext
//
//    @Autowired
//    private lateinit var authManager: AuthenticationManager
//
//    override fun configure(server: AuthorizationServerSecurityConfigurer) {
//        server.tokenKeyAccess("permitAll()")
//            .checkTokenAccess("isAuthenticated()")
//    }
//
//    override fun configure(clients: ClientDetailsServiceConfigurer) {
//        clients.jdbc(context.getBean(DataSource::class.java))
//            .withClient(env.getProperty("client-id"))
//            .secret(env.getProperty("client-secret"))
//            .authorizedGrantTypes("implicit", "authorization_code")
//            .scopes("read", "write")
//    }
//
//    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
//        endpoints.tokenStore(tokenStore())
//            .authenticationManager(authManager)
//    }
//
//    @Bean
//    @Primary
//    fun tokenServices(): DefaultTokenServices {
//        val tokenServices = DefaultTokenServices()
//        tokenServices.setTokenStore(tokenStore())
//        tokenServices.setSupportRefreshToken(true)
//        return tokenServices
//    }
//
//    @Bean
//    fun tokenStore(): TokenStore {
//        return JdbcTokenStore(context.getBean(DataSource::class.java))
//    }
//}
//
