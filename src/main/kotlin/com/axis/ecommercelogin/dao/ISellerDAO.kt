package com.axis.ecommercelogin.dao

import com.axis.ecommercelogin.model.Seller
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ISellerDAO :MongoRepository<Seller,Int>{
    fun findByEmail(email:String): Seller?
    fun existsByEmail(email: Any)
}
