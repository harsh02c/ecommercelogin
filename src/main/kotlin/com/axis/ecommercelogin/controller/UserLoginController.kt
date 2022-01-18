package com.axis.ecommercelogin.controller

import com.axis.ecommercelogin.dto.LoginDTO
import com.axis.ecommercelogin.dto.Message
import com.axis.ecommercelogin.dto.SignUpDTO
import com.axis.ecommercelogin.model.User
import com.axis.ecommercelogin.service.ILoginService
//import com.axis.ecommercelogin.util.FiegnServiceUtil
import com.axis.ecommercelogin.util.JwtUtil
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/ecommerce-auth/user")
class UserLoginController {
    @Autowired
    private lateinit var iLoginService: ILoginService
//    @Autowired
//    private lateinit var iFiegnServiceUtil:FiegnServiceUtil

    @Autowired
    private val jwtUtil: JwtUtil? = null

    @PostMapping("/signup")
    @Throws(Exception::class)
    fun signUp( @RequestBody @Valid body:  SignUpDTO, bindingResult : BindingResult):ResponseEntity<Any?>{

        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors
            val errorList: MutableList<String?> = ArrayList()
            for (f in errors) {
                errorList.add(f.defaultMessage)
            }
            return ResponseEntity(errorList, HttpStatus.OK)
        }

        val emailCheck = iLoginService.findByEmailUser(body.email.toString())

        if (emailCheck != null) {
            return ResponseEntity("Email already Exists", HttpStatus.BAD_REQUEST)
        }

        val user = User()
//        user._id = body._id.toInt()
        user.name = body.name.toString()
        user.email = body.email.toString()
        user.mobileNo = body.mobileNo.toString()
        user.password = body.password.toString()

        var signup = iLoginService.signUpUser(user)
        return ResponseEntity(signup, HttpStatus.OK)

    }

    @Throws(Exception::class)
    @PostMapping("/login")
    fun login(@RequestBody @Valid body: LoginDTO, response: HttpServletResponse,bindingResult: BindingResult): ResponseEntity<Any> {

        if (bindingResult.hasErrors()) {
            val errors = bindingResult.fieldErrors
            val errorList: MutableList<String?> = ArrayList()
            for (f in errors) {
                errorList.add(f.defaultMessage)
            }
            return ResponseEntity(errorList, HttpStatus.BAD_REQUEST)
        }
        val user = iLoginService.findByEmailUser(body.email.toString())
            ?: return ResponseEntity(Message("User not found!"), HttpStatus.UNAUTHORIZED)


        if (!user.comparePassword(body.password.toString())) {
//            return ResponseEntity.badRequest().body(Message("Invalid password!"))

            return ResponseEntity(Message("Invalid password!"), HttpStatus.UNAUTHORIZED)
        }
//        val issuer = user._id.toString()
        val issuer = user._id.toString()
        //Old code to create token
//        try {
//            val jwt = Jwts.builder()
//                .setIssuer(issuer)
//                .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
//                .signWith(SignatureAlgorithm.HS512, user.email).compact()
//        }catch  (e: Exception) {
//            println(e)
//            return ResponseEntity.status(401).body(e)
//        }
        //New code to create token

        val jwt  = jwtUtil?.generateToken(issuer)!!


        //Cookie code commented START
        val cookie = Cookie("jwt",  jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)
        //Cookie code commented END

        //Build array START(HASHMAP)
        val resp = HashMap<String, String>()
        resp["_id"] = user._id.toString()
        resp["name"] = user.name
        resp["email"] = user.email
        resp["mobileNo"] = user.mobileNo
        resp["token"] =  jwt
        //Build array END(HASHMAP)

        return ResponseEntity(resp, HttpStatus.OK)
//        return ResponseEntity.ok(Message("success"))
    }

//    @GetMapping("/user")
////    fun user(@RequestHeader("Authorization")  jwt: String?): ResponseEntity<Any> { //When token is sent from header
//    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
//        try {
//            if (jwt == null) {
//                return ResponseEntity.status(401).body(Message("unauthenticated"))
//            }
//
//            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
//            return ResponseEntity.ok(Message("Authenticated url"))
//        } catch (e: Exception) {
//            println(e)
//            return ResponseEntity.status(401).body(Message("Unauthenticated"))
//        }
//    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Successful Logout"))
    }

//    @GetMapping("getBookings")
//    fun getBookings(response: HttpServletResponse): ResponseEntity<Any> {
//
//        var getBookings =  iFiegnServiceUtil.getAllBooking()
//        return ResponseEntity(getBookings,HttpStatus.OK)
//    }
}