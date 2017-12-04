package com.paddlemax.api.controllers

import com.fasterxml.jackson.module.kotlin.*
import com.paddlemax.api.db.user.User
import com.paddlemax.api.db.user.UserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UsersController {

    @Autowired
    private lateinit var userService: UserServiceImpl

    private companion object {
        val log = LoggerFactory.getLogger(UsersController::class.java)
        val mapper = jacksonObjectMapper()
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: String): String? {
        log.debug("GET user ${id}")

        val user = userService.retrieveUser(id.toLong())
        if (user != null) {
            val userJson = mapper.writeValueAsString(user)
            log.debug("Responding with user ${mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(user)}")
            return userJson
        } else {
            log.debug("No user found with ID: ${id}")
            return null
        }
    }

    @PostMapping("/")
    fun createUser(@RequestBody user: User) {
        log.debug("POST user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(user)}")

//        val user = mapper.readValue<User>(userJson)
        userService.addUser(user)
    }

    @PutMapping("/")
    fun updateUser(userJson: String) {
        log.debug("PUt user ${mapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(userJson)}")

        val user = mapper.readValue<User>(userJson)
        userService.updateUser(user.id, user)
    }


}
