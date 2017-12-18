package com.paddlemax.api.config

import com.paddlemax.api.db.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
@Transactional
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var req: HttpServletRequest

    override fun loadUserByUsername(email: String): UserDetails {

        var userDetails: org.springframework.security.core.userdetails.User
        val authorities = ArrayList<GrantedAuthority>()

        try {
            val user = userRepo.findByEmail(email)
            if (user == null) {
                throw UsernameNotFoundException(email)
            } else {
                authorities.add(SimpleGrantedAuthority("user"))
                userDetails = org.springframework.security.core.userdetails.User(
                    user.email,
                    user.password,
                    authorities)
            }
        } catch (e: UsernameNotFoundException) {
            e.printStackTrace()
            userDetails = org.springframework.security.core.userdetails.User(
                null,
                null,
                authorities)
        }

        return userDetails
    }


    // TODO: weigh pros and cons of blockin IPs after a certain number of tries
    private fun getClientIP(): String {
        val xfHeader = req.getHeader("X-Forwarded-For") ?: return req.getRemoteAddr()
        return xfHeader.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }
}
