package com.paddlemax.api.db.user

interface UserService {

    fun findById(id: Long): User?

    fun findByEmail(email: String): User?

    fun register(user: User): User?

    fun update(user: User): User?

    fun delete(id: Long)
}
