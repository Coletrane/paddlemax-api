package com.paddlemax.api.controllers

import com.fasterxml.jackson.module.kotlin.*
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserRepository
import com.paddlemax.api.db.user.UserService
import com.paddlemax.api.db.user.UserServiceImpl
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.net.URI
import java.time.LocalDate
import javax.transaction.Transactional
import com.paddlemax.api.config.JacksonConfig
import org.junit.Assert.assertNotNull
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.AnnotationConfigContextLoader

@RunWith(SpringRunner::class)
//@SpringBootTest
@DataJpaTest
class UserControllerTest {

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var userRepo: UserRepository

    private val mapper = JacksonConfig().objectMapper()

    private lateinit var userService: UserService

    private lateinit var userController: UserController

    lateinit var cole: User
    lateinit var partialCole: User

    val notInDb = User (
        null,
        "nat king",
        "cole",
        "nat@finnesin-yo-lady.com",
        null,
        null,
        null
    )

    val notInDbJson = """
        {
            "firstName": "buddy",
            "lastName": "rich",
            "email": "buddy@finnesin-yo-lady.com"
        }
    """

    @Before
    fun setUp() {
        userService = UserServiceImpl(userRepo)
        userController = UserController(
            userService)

        // Set up test users
        cole = userService.save(User(
            null,
            "cole",
            "inman",
            "eloc49@gmail.com",
            LocalDate.of(1994, 4, 13),
            160,
            "Virginia"
        ))
        partialCole = userService.save(User(
            null,
            "coletrane",
            "johnson",
            "coletranemusic@gmail.com",
            null,
            null,
            null
        ))

        assertNotNull(cole)
        assertNotNull(partialCole)
    }

    @Test
    fun getUserUserExists() {
        val res = userController.getUser(cole.id.toString())
        val user = mapper.readValue<User>(res.body)
        assertEquals(cole.id, user.id)
        assertEquals(cole.firstName, user.firstName)
        assertEquals(cole.lastName, user.lastName)
    }

    @Test
    fun getUserUserDoesNotExist() {
        val res = userController.getUser("1000")
        assertEquals(HttpStatus.NOT_FOUND, res.statusCode)
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
            null,
            "robert",
            "glasper",
            "eloc49@gmail.com",
            null,
            null,
            null
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
