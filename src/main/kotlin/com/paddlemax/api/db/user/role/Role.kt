package com.paddlemax.api.db.user.role

import javax.persistence.*

data class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    val name: RoleEnum
)

enum class RoleEnum {
    USER
}
