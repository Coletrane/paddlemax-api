package com.paddlemax.api.db.user

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserServiceImpl(
    val userRepo: UserRepository
): UserService {

    private companion object {
        val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }

    override fun retrieveUser(id: Long): User? {
        log.debug("Retrieving user: ${id}")

        val user = userRepo.findOne(id)
        log.debug("Found user: ${userInfo(user)}")

        return user
    }

    override fun addUser(user: User): User {
        log.debug("Adding user: ${userInfo(user)}")

        return userRepo.save(user)
    }

    override fun updateUser(id: Long, user: User): User? {
        log.debug("Updating user ID: ${id} ${userInfo(user)}")

        val userToUpdate = userRepo.findOne(id)
        if (userToUpdate != null) {
            log.debug("Found user ${userInfo(userToUpdate)}")
            return userRepo.save(user)
        } else {
            log.error("User with ID: ${id} not found")
            return null
        }
    }

    fun userInfo(user: User): String {
        return "${user.firstName} ${user.lastName} with ID: ${user.id}"
    }
}
