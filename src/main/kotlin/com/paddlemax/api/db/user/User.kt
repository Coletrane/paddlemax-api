package com.paddlemax.api.db.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.paddlemax.api.db.user.role.Role
import com.paddlemax.api.db.user.role.RoleEnum
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

//    val enabled: Boolean,
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "app_user_roles",
//        joinColumns = arrayOf(
//            JoinColumn(
//                name = "user_id",
//                referencedColumnName = "id")
//        ),
//        inverseJoinColumns = arrayOf(
//            JoinColumn(
//                name = "role_id",
//                referencedColumnName = "id")
//        )
//    )
//    val roles: Array<Role>,


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
        password: String
        ): this(
            id,
            firstName,
            lastName,
            email,
            password,
            null,
            null,
            null
        )
}
