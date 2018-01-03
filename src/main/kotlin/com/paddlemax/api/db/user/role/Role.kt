package com.paddlemax.api.db.user.role

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    val name: RoleEnum
)

enum class RoleEnum {
    USER
}
