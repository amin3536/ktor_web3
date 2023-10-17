package com.example.application.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.domain.entity.User
import com.example.domain.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun  Route.auth() {
    val service by inject<UserService>()
    post("/register") {
        val user = call.receive<User>()
        val id = service.createUser(user)
        call.respond(HttpStatusCode.Created, hashMapOf("id" to id))
    }
//    val myRealm =   environment?.config?.property("jwt.realm")?.getString()
    post("/login") {
        val secret =    call.application.environment.config.property("jwt.secret").getString()
        val issuer =     call.application.environment.config.property("jwt.issuer").getString()
        val audience =   call.application.environment.config.property("jwt.audience").getString()
        val user=call.receive<User>()
        if (service.verifyUser(user)){
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("mail", user.mail)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret))
            return@post call.respond(HttpStatusCode.OK,hashMapOf("token" to token ))
        }
        throw RuntimeException("something happen please wait")
    }
}