package com.aa.airportagent.demo

import com.azure.spring.aad.webapi.AADJwtBearerTokenAuthenticationConverter
import com.microsoft.aad.msal4j.ClientCredentialFactory
import com.microsoft.aad.msal4j.ConfidentialClientApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.client.RestTemplate

@Configuration
class Configuration {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty("app.security.enabled", havingValue = "true")
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ?.and()
            ?.authorizeRequests()
            ?.antMatchers("/secure/**")?.authenticated()
            ?.antMatchers("/**")?.permitAll()
            ?.anyRequest()?.authenticated()
            ?.and()
            ?.oauth2ResourceServer()?.jwt()?.jwtAuthenticationConverter(AADJwtBearerTokenAuthenticationConverter())
        http?.exceptionHandling()?.authenticationEntryPoint(ProblemDetailsAuthenticationEntryPoint())
            ?.accessDeniedHandler(GlobalAccessDeniedHandler())
    }
}

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@ConditionalOnProperty("app.security.enabled", havingValue = "false")
class NoSecurityConfig: WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()
        http?.authorizeRequests()
            ?.antMatchers("/**")?.permitAll()
    }
}