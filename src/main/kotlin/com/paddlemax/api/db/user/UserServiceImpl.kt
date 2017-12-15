package com.paddlemax.api.db.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class  UserServiceImpl: UserService {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userRepo: UserRepository

    override fun findById(id: Long): User? {
        return userRepo.findById(id)
    }

    override fun findByEmail(email: String): User? {
        return userRepo.findByEmail(email)
    }

    override fun register(user: User): User? {

        var result: User? = null

        //TODO: throw an exception here
        val userInDb = findByEmail(user.email)
        if (userInDb == null) {
            val registeredUser = User(
                -1,
                user.firstName,
                user.lastName,
                user.email,
                passwordEncoder.encode(user.password),
                user.birthday,
                user.weightLbs,
                user.location
            )

            result =  userRepo.save(registeredUser)
        }

        return result
    }

    override fun update(user: User): User? {

        var result: User? = null

        val userInDb = findByEmail(user.email)
        if (userInDb != null) {
            result = userRepo.save(user)
        }

        return result
    }

    override fun delete(id: Long) {
        userRepo.delete(id)
    }
}
