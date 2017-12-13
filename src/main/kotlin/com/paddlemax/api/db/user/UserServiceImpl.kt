package com.paddlemax.api.db.user

import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class  UserServiceImpl(
    val userRepo: UserRepository
): UserService {

    override fun findById(id: Long): User? {
        return userRepo.findById(id)
    }

    override fun findByEmail(email: String): User? {
        return userRepo.findByEmail(email)
    }

    override fun save(user: User): User {
        return userRepo.save(user)
    }

    override fun delete(id: Long) {
        userRepo.delete(id)
    }
}
