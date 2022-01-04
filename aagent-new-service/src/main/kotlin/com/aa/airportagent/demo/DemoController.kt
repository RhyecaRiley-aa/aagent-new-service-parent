package com.aa.airportagent.demo

import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@RestController
//@RequestMapping("/secure")
class DemoController {

    private val counter = AtomicLong()
    @Operation(summary = "Get a custom greeting")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Returning a greeting",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = Demo::class))]
        )]
    )
    @GetMapping(value = ["/greeting"], produces = ["application/json"])
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String?): Demo {
        return Demo(counter.incrementAndGet(), String.format(template, "$name"))
    }

    companion object {
        private const val template = "Hello, %s!"
    }
}
