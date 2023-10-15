package com.example.domain.service

import com.example.domain.entity.User

interface UserService {
    suspend  fun createUser(user: User): Int
    suspend fun verifyUser(user: User):Boolean
}