package com.example.infrastructure.plugins

import com.example.application.routes.auth
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!2")
        }
        auth()
    }
}
