package com.example.domain.entity

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable



@Serializable
data class User(var mail: String, var password:String,var id : Int?=null ,var state:State ): Principal
enum class State(){
    ACTIVE,IN_ACTIVE
}

interface UserRepository {

    suspend fun create(user: User): Int

    suspend fun read(id: Int): User?

    suspend fun update(id: Int, user: User)

    suspend fun delete(id: Int)

    suspend fun findByEmail(mail: String ): User?
}