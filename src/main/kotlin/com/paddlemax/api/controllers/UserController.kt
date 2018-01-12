package com.paddlemax.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.nio.charset.Charset
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    private companion object {
        val log = LoggerFactory.getLogger(UserController::class.java)
    }

    @GetMapping(
        "/me",
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getUser(req: HttpServletRequest): ResponseEntity<String> {

        val base64Credentials =
            req.getHeader(HttpHeaders.AUTHORIZATION)
                .substring("Basic".length)
                    .trim()

        val credentials =
            String(
                Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"))
                .split(":")

        val email = credentials[0]

        log.info("GET user with email: $email")

        val response: ResponseEntity<String>

        val user = userService.findByEmail(email)
        if (user != null) {

            val userNoPw = User(user, null)

            log.info("Responding with user ${mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(userNoPw)}")

            response = ResponseEntity
                .ok(mapper.writeValueAsString(userNoPw))
        } else {
            // I think this is unreachable because of auth
            log.info("No user found with email: $email")
            response = ResponseEntity
                .notFound()
                .build()
        }

        return response
    }

    @PostMapping("/register",
        consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun register(
        @RequestBody user: User,
        req: HttpServletRequest): ResponseEntity<String> {

        log.info("POST user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(user)}")

        val res: ResponseEntity<String>

        val newUser = userService.register(user)
        if (newUser != null) {
            log.info("Saving of user: ${userInfo(newUser)} was successful")
            // Returning this so the clients can get the new user ID
            res = ResponseEntity
                .created(URI("/user/${newUser.id}"))
                .build()
        } else {
            val errStr = "User ${userInfo(user)} already exists"
            log.info(errStr)
            res = ResponseEntity
                .badRequest()
                .body(errStr)
        }

        return res
    }

    @PutMapping(
        consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun updateUser(
        @RequestBody partialUserInfo: User): ResponseEntity<String> {

        log.info("PUT user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(partialUserInfo)}")

        val response: ResponseEntity<String>

        // Make sure the user already exists
        val userInDb = userService.findByEmail(partialUserInfo.email)

        if (userInDb != null) {
            log.info("Found user ${userInfo(userInDb)}")
            // Make a new user with the id from the user we just got
            val userToUpdate = User(
                userInDb.id,
                partialUserInfo.firstName,
                partialUserInfo.lastName,
                partialUserInfo.email,
                userInDb.password,
                partialUserInfo.birthday,
                partialUserInfo.weightLbs,
                partialUserInfo.location
            )

            val updatedUser = userService.update(userToUpdate)

            log.info("Updating of user: ${userInfo(updatedUser)} was successful")
            response = ResponseEntity
                .ok(mapper.writeValueAsString(updatedUser))

        } else {
            log.error("User with ID: ${partialUserInfo.id} not found")
            response = ResponseEntity
                .notFound()
                .build()
        }

        return response
    }

    private fun userInfo(user: User?): String {
        return "${user?.firstName} ${user?.lastName} with ID: ${user?.id}"
    }
}
