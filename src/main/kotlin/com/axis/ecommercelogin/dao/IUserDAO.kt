package com.axis.ecommercelogin.dao

import com.axis.ecommercelogin.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserDAO :MongoRepository<User,Int>{
    fun findByEmail(email:String): User?
    fun existsByEmail(email: Any)
}
