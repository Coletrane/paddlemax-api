package com.paddlemax.api.config

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPoint: BasicAuthenticationEntryPoint() {

    override fun commence(
        req: HttpServletRequest?,
        res: HttpServletResponse?,
        authE: AuthenticationException?) {

        res?.addHeader("WWW-Authenitcate", "Basic realm=${getRealmName()}")
        res?.status = HttpServletResponse.SC_UNAUTHORIZED
        res?.writer?.println("HTTP Status 401 ${authE?.message}")
    }

    override fun afterPropertiesSet() {
        realmName = "paddlemax-user"
        super.afterPropertiesSet()
    }

}
