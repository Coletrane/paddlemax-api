package com.paddlemax.api.db.user

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "app_user")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = -1,

    val firstName: String,

    val lastName: String,

    @Column(unique = true)
    val email: String,

    @JsonFormat(pattern = "yyyy-MM-dd")
    val birthday: LocalDate?,

    val weightLbs: Int?,

    val location: String?
    )
