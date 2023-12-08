package com.sasha.authservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
internal class SecurityConfig(private val keycloakLogoutHandler: KeycloakLogoutHandler) {

  @Bean
  protected fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
    return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
  }

  @Order(1)
  @Bean
  @Throws(Exception::class)
  fun clientFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests {
        it
          .requestMatchers(AntPathRequestMatcher("/"))
          .permitAll()  // Allow any requests for home page
          .anyRequest() // For all other requests
          .authenticated()  // Require auth
      }
      .oauth2Login {}
      .logout {
        it
          .addLogoutHandler(keycloakLogoutHandler)
          .logoutSuccessUrl("/")
      }

    return http.build()
  }

  @Order(2)
  @Bean
  @Throws(Exception::class)
  fun resourceServerFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .authorizeHttpRequests {
        it
          .requestMatchers(AntPathRequestMatcher("/customers*"))
          .hasRole("USER")
          .anyRequest()
          .authenticated()
      }

    http.oauth2ResourceServer { oauth2: OAuth2ResourceServerConfigurer<HttpSecurity?> ->
      oauth2.jwt(Customizer.withDefaults())
    }

    return http.build()
  }

  @Bean
  @Throws(Exception::class)
  fun authenticationManager(http: HttpSecurity): AuthenticationManager {
    return http.getSharedObject(AuthenticationManagerBuilder::class.java)
      .build()
  }
}