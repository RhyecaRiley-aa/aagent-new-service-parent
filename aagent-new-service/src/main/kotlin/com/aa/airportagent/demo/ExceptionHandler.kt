package com.aa.airportagent.demo

import com.aa.airportagent.sabre.model.InvalidParam
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class ProblemDetails(
    val type: URI, val title: String, val status: Int,
    val detail: String?, val secondaryDetails: String?,
    val instance: URI, val invalidParams: List<InvalidParam> = emptyList()
)

private val objectMapper = ObjectMapper().registerModule(KotlinModule())

class ProblemDetailsAuthenticationEntryPoint: AuthenticationEntryPoint {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, ex: AuthenticationException) {
        log.error(ex.message, this)
        val problemDetails = ex.toProblemDetails(HttpStatus.FORBIDDEN, URI.create(request.requestURI))
        log.debug("ProblemDetails: $problemDetails")
        response.status = problemDetails.status
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
        objectMapper.writeValue(response.outputStream, problemDetails)
        response.outputStream.flush()
    }
}

class GlobalAccessDeniedHandler: AccessDeniedHandler {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, ex: AccessDeniedException) {
        log.error(ex.message, this)
        val problemDetails = ex.toProblemDetails(HttpStatus.FORBIDDEN, URI.create(request.requestURI))
        log.debug("ProblemDetails: $problemDetails")
        response.status = problemDetails.status
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
        objectMapper.writeValue(response.outputStream, problemDetails)
        response.outputStream.flush()
    }
}

fun Throwable.toProblemDetails(status: HttpStatus, instance: URI): ProblemDetails {
    val type = when {
        status.is4xxClientError -> URI.create("https://tools.ietf.org/html/rfc7231#section-6.5.1")
        else -> URI.create("https://tools.ietf.org/html/rfc7231#section-6.6.1")
    }
    return ProblemDetails(type, status.reasonPhrase, status.value(), message, cause?.message, instance)
}

fun Throwable.toProblemDetails(type: URI, status: HttpStatus, secondaryDetails: String?, instance: URI): ProblemDetails {
    return ProblemDetails(type, status.reasonPhrase, status.value(), message, secondaryDetails, instance)
}
