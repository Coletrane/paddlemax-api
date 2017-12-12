package com.paddlemax.api

import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.json.JacksonJsonParser
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.jdbc.datasource.init.DatabasePopulator
import org.springframework.util.LinkedMultiValueMap
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.StringUtils
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource
import javax.sql.DataSource
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = arrayOf(PaddleMaxApplication::class),
    webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaddleMaxApplicationTest {

    @Autowired
    private lateinit var env: Environment

//    @Autowired
//    private lateinit var tokenStore: TokenStore
//
//    @Autowired
//    private lateinit var clientDetailsServ: ClientDetailsService
//
//    @Autowired
//    private lateinit var tokenServices: DefaultTokenServices

    @Autowired
    private lateinit var mvc: MockMvc

    private companion object {
        val TOKEN_ENDPOINT = "/oauth/token"
        val AUTHORIZE_ENDPOINT = "/oauth/authorize"
        val TEST_USERNAME = "cole"
        val TEST_PASSWORD = "assword"
        val jsonParser = JacksonJsonParser()
    }

    private lateinit var clientId: String
    private lateinit var clientSecret: String

    private fun getOauthToken(
        username: String,
        password: String): String {

        val res = mvc.perform(
            post(TOKEN_ENDPOINT)
                .param("grant_type", "password")
                .param("client_id", clientId)
                .param("username", TEST_USERNAME)
                .param("password", TEST_PASSWORD)
                .with(httpBasic(clientId, clientSecret))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val resStr = res
            .andReturn()
            .response
            .contentAsString

        return jsonParser.parseMap(resStr).get("access_token").toString()
    }

    @Before
    fun setUp() {
        clientId = env.getProperty("client-id")
        clientSecret = env.getProperty("client-secret")
    }

    @Test
    fun notAuthorized() {
        mvc.perform(
            get("/users/1"))
            .andExpect(status().isUnauthorized)
    }
//    @Test
//    fun getOauthToken() {
////        val userJson = """
////            {
////                "firstName": "cole",
////                "lastName": "inman",
////                "email": "eloc49@gmail.com"
////            }
////        """
////        mvc.perform(
////            post("/user")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(userJson)
////                .accept(MediaType.APPLICATION_JSON))
////            .andExpect(status().isCreated)
////
////        mvc.perform(
////            get("/user/1")
////                .accept(MediaType.APPLICATION_JSON))
////            .andReturn()
////            .response
//
////        mvc.perform(get(AUTHORIZE_ENDPOINT)
////            .param("response_type", "code")
////            .param("client_id", clientId))
//
////        val tokenStr = getOauthToken(TEST_USERNAME, TEST_PASSWORD)
////        assertFalse(StringUtils.isEmpty(tokenStr))
//    }

}




