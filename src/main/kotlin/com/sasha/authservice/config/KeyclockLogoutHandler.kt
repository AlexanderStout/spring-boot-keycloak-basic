package com.sasha.authservice.config

import com.sasha.authservice.delegates.LoggerDelegate
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import com.sasha.authservice.extensions.logger
import org.springframework.context.annotation.Bean


@Component
class KeycloakLogoutHandler(private val restTemplate: RestTemplate) : LogoutHandler {

    private val logger by LoggerDelegate()

    override fun logout(
        request: HttpServletRequest?, response: HttpServletResponse?,
        auth: Authentication
    ) {
        logoutFromKeycloak(auth.getPrincipal() as OidcUser)
    }

    private fun logoutFromKeycloak(user: OidcUser) {
        val endSessionEndpoint = user.issuer.toString() + "/protocol/openid-connect/logout"
        val builder = UriComponentsBuilder
            .fromUriString(endSessionEndpoint)
            .queryParam("id_token_hint", user.idToken.tokenValue)
        val logoutResponse = restTemplate.getForEntity(
            builder.toUriString(), String::class.java
        )
        if (logoutResponse.statusCode.is2xxSuccessful) {
            logger.info("Successfulley logged out from Keycloak")
        } else {
            logger.error("Could not propagate logout to Keycloak")
        }
    }
}