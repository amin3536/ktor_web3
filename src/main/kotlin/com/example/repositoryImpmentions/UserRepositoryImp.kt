package com.example.repositoryImpmentions

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.annotation.Singleton
import com.example.domain.entity.User
import com.example.domain.entity.UserRepository


@Singleton(binds = [UserRepository::class])
class UserRepositoryImp(database:Database) : BaseRepository(), UserRepository {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val mail = varchar("mail", length = 150).uniqueIndex()
        val password = varchar("password",length = 150)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }



    override suspend fun create(user: User): Int = dbQuery {
        Users.insert {
            it[mail] = user.mail
            it[password] = user.password
        }[Users.id]
    }

    override suspend fun read(id: Int): User? {
        return dbQuery {
            Users.select { Users.id eq id }
                .convertTOUser()
                .singleOrNull()
        }
    }

    override suspend fun update(id: Int, user: User) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[mail] = user.mail
                it[password] = user.password
            }
        }
    }

    override suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }

    override suspend fun findByEmail(mail: String ): User? {
        return dbQuery {
            Users.select { Users.mail eq mail }
                .convertTOUser()
                .singleOrNull()
        }
    }

    private fun Query.convertTOUser()=this.map{ User(it[Users.mail], it[Users.password],it[Users.id]) }
}