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

@RunWith(SpringRunner::class)
@SpringBootTest(
    classes = arrayOf(PaddleMaxApplication::class),
    webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaddleMaxApplicationTest {

    @Autowired
    private lateinit var env: Environment

    @Autowired
    private lateinit var mvc: MockMvc

    private companion object {
        val username = "cole"
        val password = "assword"
        val coleJson = """
            {
                "firstName": "cole",
                "lastName": "inman",
                "email": "eloc49@gmail.com"
            }

        """
    }

    @Test
    fun notAuthorizedInvalidHttpMethod() {
        mvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(coleJson)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun notAuthorizedValidHttpMethod() {
        mvc.perform(
            get("/user/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }
}




