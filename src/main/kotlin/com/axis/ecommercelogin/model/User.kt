package com.axis.ecommercelogin.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Document(collection = "user")
class User {

    @Id
    var _id: String? = null
    //    var _id:Int=0
    var name=""
    var mobileNo=""
    var email=""
    var type=""
    var password=""
        @JsonIgnore
        get()=field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password,this.password)
    }
}