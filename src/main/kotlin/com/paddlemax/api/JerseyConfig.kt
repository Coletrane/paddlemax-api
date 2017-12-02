package com.paddlemax.api

import com.paddlemax.api.endpoints.UserEndpoint
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component

@Component
class JerseyConfig: ResourceConfig {

    constructor() {
        // Endpoints
        register(UserEndpoint::class.java)

        //TODO: logging
    }
}
