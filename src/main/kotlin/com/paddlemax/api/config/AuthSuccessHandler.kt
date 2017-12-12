package com.paddlemax.api.config

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val requestCache = HttpSessionRequestCache()

    override fun onAuthenticationSuccess(
        req: HttpServletRequest?,
        res: HttpServletResponse?,
        auth: Authentication?) {

        if (requestCache.getRequest(req, res) != null) {
            clearAuthenticationAttributes(req)
            return
        }

        if (isAlwaysUseDefaultTargetUrl ||
            targetUrlParameter != null &&
            StringUtils.hasText(req?.getParameter(targetUrlParameter))) {
            requestCache.removeRequest(req, res)
            clearAuthenticationAttributes(req)
            return
        }

        clearAuthenticationAttributes(req)
    }
}
