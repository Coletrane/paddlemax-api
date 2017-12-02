package com.paddlemax.api.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class User(

    @JsonProperty("firstName")
    val firstName: String,

    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("birthday")
    val birthday: Date,

    @JsonProperty("id")
    val id: String
)
