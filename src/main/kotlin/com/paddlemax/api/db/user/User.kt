package com.paddlemax.api.db.user

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    val firstName: String,

    val lastName: String,

    @Column(unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String?,


    @JsonFormat(pattern = "yyyy-MM-dd")
    val birthday: LocalDate?,

    val weightLbs: Int?,

    val location: String?) {

    // Conveinence constructor
    constructor(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        password: String): this(
            id,
            firstName,
            lastName,
            email,
            password,
            null,
            null,
            null
        )

    constructor(user: User): this(
        user.id,
        user.firstName,
        user.lastName,
        user.email,
        user.password,
        user.birthday,
        user.weightLbs,
        user.location
    )

    constructor(user: User, password: String?): this(
        user.id,
        user.firstName,
        user.lastName,
        user.email,
        password,
        user.birthday,
        user.weightLbs,
        user.location
    )
}
