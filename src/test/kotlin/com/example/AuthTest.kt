package com.example

import com.example.domain.entity.User
import com.example.infrastructure.plugins.configureRouting
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.testing.*
import io.ktor.util.logging.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.event.Level

class AuthTest : BaseTest() {

    @Test
    fun testAuth() = testApplication {
        install(CallLogging) { level = Level.INFO }
        application {
            configureRouting()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/register") {
            headers {
                contentType(ContentType.Application.Json)
            }
            setBody(User("aminmir@gmail.com", "123455"))
        }
        val body=response.bodyAsText().toString()
        println(body)

        Assertions.assertEquals(HttpStatusCode.Created, response.status)

        val loginResponse = client.post("/login") {
            headers {
                contentType(ContentType.Application.Json)
            }
            setBody(User("aminmir@gmail.com", "123455"))
        }
        val loginBody=response.bodyAsText()
        println(loginBody)

        Assertions.assertEquals(HttpStatusCode.OK, loginResponse.status)
    }

}