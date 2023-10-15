package com.example.application.routes

import com.auth0.jwt.JWT
import com.example.domain.entity.User
import com.example.domain.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun  Route.auth() {
    val service by inject<UserService>()
    post("/register") {
        val user=call.receive<User>()
        val id=service.createUser(user)
        call.respond(HttpStatusCode.Created,id)
    }
    post("/login") {
        val user=call.receive<User>()
        if (service.verifyUser(user)){

        }
    }
}