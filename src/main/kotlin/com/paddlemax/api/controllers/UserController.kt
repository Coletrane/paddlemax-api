package com.paddlemax.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.lang.Long.parseLong
import java.net.URI
import javax.print.attribute.standard.Media
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired
    private var userService: UserService) {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    private companion object {
        val log = LoggerFactory.getLogger(UserController::class.java)
    }

    @GetMapping(
        "/{id}",
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getUser(@PathVariable("id") id: String): ResponseEntity<String> {

        log.info("GET user $id")

        val response: ResponseEntity<String>

        val user = userService.findById(id.toLong())
        if (user != null) {
            log.info("Responding with user ${mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(user)}")

            response = ResponseEntity
                .ok(mapper.writeValueAsString(user))
        } else {
            log.info("No user found with ID: ${id}\"")
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
        @Valid user: User,
        req: HttpServletRequest): ResponseEntity<String> {

        log.info("POST user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(user)}")

        checkUserExists(user)

        val res: ResponseEntity<String>
        val newUser = userService.save(user)

        if (newUser != null) {
            log.info("Saving of user: ${userInfo(newUser)} was successful")
            // Returning this so the clients can get the new user ID
            res = ResponseEntity
                .created(URI("/user/${newUser.id}"))
                .build()
        } else {
            val errStr = "User ${userInfo(newUser)} already exists"
            log.info(errStr)
            res = ResponseEntity
                .badRequest()
                .body(errStr)
        }

//        eventPublisher.publishEvent(OnRegistrationCompleteEvent(newUser, req.locale))
        return res
    }

    @PutMapping(
        consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE),
        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun updateUser(@RequestBody partialUserInfo: User): ResponseEntity<String> {

        log.info("PUT user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(partialUserInfo)}")

        val response: ResponseEntity<String>

        // Make sure the user already exists
        val persistentUser = userService.findByEmail(partialUserInfo.email)

        if (persistentUser != null) {
            log.info("Found user ${userInfo(persistentUser)}")
            // Make a new user with the id from the user we just got
            val userToUpdate = User(
                persistentUser.id,
                partialUserInfo.firstName,
                partialUserInfo.lastName,
                partialUserInfo.email,
                partialUserInfo.birthday,
                partialUserInfo.weightLbs,
                partialUserInfo.location
            )

            val updatedUser = userService.save(userToUpdate)

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

    private fun userInfo(user: User): String {
        return "${user.firstName} ${user.lastName} with ID: ${user.id}"
    }

    private fun checkUserExists(user: User): ResponseEntity<String>? {

        var res: ResponseEntity<String>? = null

        if (userService.findByEmail(user.email) != null) {
            res = ResponseEntity
                .badRequest()
                .body("User already exists")
        }

        return res
    }
}
