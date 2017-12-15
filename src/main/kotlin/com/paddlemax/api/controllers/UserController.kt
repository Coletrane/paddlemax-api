package com.paddlemax.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserService
import com.paddlemax.api.db.user.UserServiceImpl
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

    // TODO: change to /me endpoint
//    @GetMapping(
//        "/{id}",
//        produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
//    fun getUser(@PathVariable("id") id: String): ResponseEntity<String> {
//
//        log.info("GET user $id")
//
//        val response: ResponseEntity<String>
//
//        val user = userService.findById(id.toLong())
//        if (user != null) {
//            log.info("Responding with user ${mapper
//                .writerWithDefaultPrettyPrinter()
//                .writeValueAsString(user)}")
//
//            response = ResponseEntity
//                .ok(mapper.writeValueAsString(user))
//        } else {
//            log.info("No user found with ID: ${id}\"")
//            response = ResponseEntity
//                .notFound()
//                .build()
//        }
//
//        return response
//    }

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
