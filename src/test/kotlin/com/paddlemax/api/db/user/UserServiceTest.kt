package com.paddlemax.api.db.user

import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    private companion object {
        val coltrane = User(
            -1,
            "John",
            "Coltrane",
            "john@finnesinyolady.com",
            "alovesupreme",
            null,
            null,
            null
        )
    }

    @Test
    fun registerUserNotInDb() {
        assertNotNull(userService.register(coltrane))
    }

    @Test
    fun registerUserAlreadyInDb() {
        userService.register(coltrane)
        assertNull(userService.register(coltrane))
    }
}
