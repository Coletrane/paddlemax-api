package com.paddlemax.api.db.user

interface UserService {

    fun retrieveUser(id: Long): User?

    fun addUser(user: User): User

    fun updateUser(id: Long, user: User): User?
}
