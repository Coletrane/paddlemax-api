package com.paddlemax.api.endpoints

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.paddlemax.api.entities.User
import java.text.SimpleDateFormat
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
class UserEndpoint {

    @GET
    @Path("{id}")
    fun getUser(@PathParam("id") id: String): String {
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        val bday = "04/13/1994"
        val user = User(
            "Cole",
            "Inman",
            sdf.parse(bday),
            "100"
        )
        val mapper = jacksonObjectMapper()
        val userJson = mapper.writeValueAsString(user)
        return userJson
    }
}
