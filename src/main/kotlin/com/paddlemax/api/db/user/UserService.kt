package com.paddlemax.api.db.user

interface UserService {

    fun findById(id: Long): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun delete(id: Long)
}
