package com.axis.ecommercelogin.service

import com.axis.ecommercelogin.model.Seller
import com.axis.ecommercelogin.model.User
import java.util.*

interface ILoginService {
    fun signUpUser(user:User):User?
    fun signUpSeller(seller: Seller):Seller?
    fun login(user:User):User?
    fun findByEmailUser(email:String): User?
    fun findByEmailSeller(email:String): Seller?
    fun getById(id: Int): Optional<User?>
}