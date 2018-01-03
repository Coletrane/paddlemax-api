package com.paddlemax.api.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import com.paddlemax.api.PaddleMaxApplicationTest
import com.paddlemax.api.config.JacksonConfig
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserRepository
import com.paddlemax.api.db.user.UserService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var userRepo: UserRepository

    private val mapper = JacksonConfig().objectMapper()

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var mvc: MockMvc

    private val cole = User(
        -1,
        "cole",
        "inman",
        "eloc49@gmail.com",
        "SpacesNotTabs!1",
        LocalDate.of(1994, 4, 13),
        160,
        "Virginia"
    )

    private val partialCole = User(
        -1,
        "coletrane",
        "johnson",
        "coletranemusic@gmail.com",
        "SgtJohnson!1"
    )

    val notInDb = User (
        -1,
        "nat king",
        "cole",
        "nat@finnesin-yo-lady.com",
        "StraightenUpAndFlyRight1!"
    )

    val notInDbJson = """
        {
            "firstName": "buddy",
            "lastName": "rich",
            "email": "buddy@finnesin-yo-lady.com",
            "password": "MexicaliNoise1!"
        }
    """

    @Before
    fun setUp() {
        // Set up test users
        val savedCole = userService.register(cole)
        val savedPartialCole = userService.register(partialCole)

        assertNotNull(savedCole)
        assertNotNull(savedPartialCole)
    }

    @Test
    fun getUser() {
        val res = mvc.perform(
            get("/user/me")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    PaddleMaxApplicationTest.authHeaderVal(
                        cole.email,
                        cole.password))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val user = mapper.readValue<User>(res)
        assertNull(user.password)
        assertEquals(cole.email, user.email)
        assertEquals(cole.firstName, user.firstName)
        assertEquals(cole.lastName, user.lastName)
    }

    @Test
    fun createUserUserDoesNotExist() {
        val res = userController.register(notInDb, MockHttpServletRequest())
        assertEquals(HttpStatus.CREATED, res.statusCode)
    }

    @Test
    fun createUserUserDoesExist() {
        val res = userController.register(cole, MockHttpServletRequest())
        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
    }

    @Test
    fun createUserWithJsonDate() {
        val user = mapper.readValue<User>(notInDbJson)
        val res = userController.register(user, MockHttpServletRequest())
        assertEquals(HttpStatus.CREATED, res.statusCode)
    }

    @Test
    fun updateUserUserExists() {
        val newUser = User(
            -1,
            "robert",
            "glasper",
            "eloc49@gmail.com",
            ""
        )
        val res = userController.updateUser(newUser)
        assertEquals(HttpStatus.OK, res.statusCode)
        val resUser = mapper.readValue<User>(res.body)
        assertEquals(newUser.firstName, resUser.firstName)
        assertEquals(newUser.lastName, resUser.lastName)
    }

    @Test
    fun updateUserUserDoesNotExist() {
        val res = userController.updateUser(notInDb)
        assertEquals(HttpStatus.NOT_FOUND, res.statusCode)
    }
}
