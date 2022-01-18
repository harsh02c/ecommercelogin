package com.axis.ecommercelogin.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

//class LoginDTO {
//    @field:NotBlank(message = "Name field is mandatory")
//    val email =""
//    val password = ""
//}

data class LoginDTO(
    @field:NotBlank(message = "Email field is mandatory")
    @field:Email(message = "Email field is Invalid")
    val email:String?=null,
    @field:NotBlank(message = "Password field is mandatory")
    val password:String?=null,
)