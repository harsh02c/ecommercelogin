package com.axis.ecommercelogin.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.*

//class SignUpDTO {
//    val _id= ""
//    @field:NotBlank(message = "MobileNo is mandatory")
//    val mobileNo= ""
//    @field:NotBlank(message = "Name is mandatory")
//    val name=""
//    @field:NotBlank(message = "Email is mandatory")
//    @field:Email(message = "Email is Not valid")
//    val email =""
//    val password = ""
//
//}
data class SignUpDTO(
    @Id
    var _id:ObjectId?=null,
    @field:NotBlank(message = "Name field is mandatory")
    var name:String?=null,
    @field:NotBlank(message = "Mobile Number field is mandatory")
//     @field:Pattern(regexp="(^\\+[0-9]{10,13}\$)",message = "Mobile Number should be digits only")
    var mobileNo:String?=null,
    @field:NotBlank(message = "Email field is mandatory")
    @field:Email(message = "Email is Not valid")
    var email:String?=null,

    @field:NotBlank(message = "Password field is mandatory")
    var password:String?=null,
)