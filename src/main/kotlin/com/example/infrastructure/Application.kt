package com.example.infrastructure

import com.example.infrastructure.plugins.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(MyModule().module)
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, e ->
            val response = TextContent(e.message ?: "Bad Request",
                ContentType.Text.Plain.withCharset(Charsets.UTF_8),
                HttpStatusCode.BadRequest
            )
            call.respond(response)
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Exception occurred: ${cause.message}")
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }

    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureSockets()
    configureRouting()
}
