package com.example.domain.service

import com.example.domain.entity.User
import com.example.domain.entity.UserRepository
import com.toxicbakery.bcrypt.Bcrypt
import org.koin.core.annotation.Singleton
import java.nio.charset.Charset

@Singleton(binds = [UserService::class])
class UserServiceImplementation(private val repository: UserRepository) : UserService {

    companion object{
        val SALT=64;
    }
    override suspend  fun createUser(user:User): Int {
        user.password=Bcrypt.hash(user.password,SALT).toString(Charset.defaultCharset())
        return repository.create(user)
    }

    override suspend fun verifyUser(user: User):Boolean {
        val result=repository.findByEmail(user.mail)
        result?: throw IllegalArgumentException("\"email\": \"email not found\"")
        return result.password.let {
            if (!Bcrypt.verify(user.password,it.toByteArray()))
                throw IllegalArgumentException("\"password\": \"email or  password is wrong\"")
            true
        }
    }

}