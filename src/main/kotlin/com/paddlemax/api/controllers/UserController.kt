package com.paddlemax.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.*
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.Long.parseLong
import java.net.URI

@RestController
@RequestMapping("/user")
class UserController(
    @Autowired
    private var userService: UserService,
    @Autowired
    private var mapper: ObjectMapper) {

    private companion object {
        val log = LoggerFactory.getLogger(UserController::class.java)
    }

    @GetMapping(
        "/{id}",
        produces = arrayOf("application/json"))
    fun getUser(@PathVariable("id") id: String): ResponseEntity<String> {

        log.debug("GET user $id")

        val response: ResponseEntity<String>

        println("PARSE LONG ${parseLong(id)}")
        val user = userService.findById(id.toLong())
        if (user != null) {
            log.debug("Responding with user ${mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(user)}")

            response = ResponseEntity
                .ok(mapper.writeValueAsString(user))
        } else {
            log.debug("No user found with ID: ${id}\"")
            response = ResponseEntity
                .notFound()
                .build()
        }

        return response
    }

    @PostMapping(
        "/",
        consumes = arrayOf("application/json"),
        produces = arrayOf("application/json"))
    fun createUser(@RequestBody user: User): ResponseEntity<String> {

        log.debug("POST user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(user)}")

        // Short circut if the ID already exists
        if (userService.findByEmail(user.email) != null) {
            return ResponseEntity
                .badRequest()
                .body("User already exists")
        }

        val response: ResponseEntity<String>

        val newUser = userService.save(user)

        if (newUser != null) {
            log.debug("Saving of user: ${userInfo(newUser)} was successful")
            // Returning this so the clients can get the new user ID
            response = ResponseEntity
                .created(URI("/user/${newUser.id}"))
                .build()
        } else {
            val errStr = "User ${userInfo(newUser)} already exists"
            log.debug(errStr)
            response = ResponseEntity
                .badRequest()
                .body(errStr)
        }

        return response
    }

    @PutMapping(
        "/",
        consumes = arrayOf("application/json"),
        produces = arrayOf("application/json"))
    fun updateUser(@RequestBody partialUserInfo: User): ResponseEntity<String> {

        log.debug("PUT user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(partialUserInfo)}")

        val response: ResponseEntity<String>

        // Make sure the user already exists
        val persistentUser = userService.findByEmail(partialUserInfo.email)

        if (persistentUser != null) {
            log.debug("Found user ${userInfo(persistentUser)}")
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

            log.debug("Updating of user: ${userInfo(updatedUser)} was successful")
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

}
