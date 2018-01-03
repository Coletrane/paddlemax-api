package com.paddlemax.api

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.Base64Utils

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PaddleMaxApplicationTest {

    @Autowired
    private lateinit var env: Environment

    @Autowired
    private lateinit var mvc: MockMvc

    companion object {
        val email = "eloc49@gmail.com"
        val password = "assword"

        // Mmmm look at that sweet, sweet string interpolation
        fun authHeaderVal(email: String, password: String?): String {
            return "Basic ${Base64Utils.encodeToString("${email}:${password}".toByteArray())}"
        }

        val coleJson = """
            {
                "firstName": "cole",
                "lastName": "inman",
                "email": "${email}",
                "password": "${password}"
            }
        """

        val updateJson = """
            {
                "firstName": "art",
                "lastName": "blakey",
                "email": "${email}"
            }
        """
    }

    @Test
    fun notAuthorized() {
        mvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(coleJson)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun authorized() {
        mvc.perform(
            post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(coleJson)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)

        mvc.perform(
            put("/user")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    authHeaderVal(email, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }
}




