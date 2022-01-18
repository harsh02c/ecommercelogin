package com.axis.ecommercelogin.controller

import com.axis.ecommercelogin.dto.LoginDTO
import com.axis.ecommercelogin.dto.Message
import com.axis.ecommercelogin.dto.SignUpDTO
import com.axis.ecommercelogin.model.Seller
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
@RequestMapping("/ecommerce-auth/seller")
class SellerLoginController {
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

        val emailCheck = iLoginService.findByEmailSeller(body.email.toString())

        if (emailCheck != null) {
            return ResponseEntity("Email already Exists", HttpStatus.BAD_REQUEST)
        }

        val seller = Seller()
//        user._id = body._id.toInt()
        seller.name = body.name.toString()
        seller.email = body.email.toString()
        seller.mobileNo = body.mobileNo.toString()
        seller.password = body.password.toString()

        var signup = iLoginService.signUpSeller(seller)
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
        val seller = iLoginService.findByEmailSeller(body.email.toString())
            ?: return ResponseEntity(Message("Seller not found!"), HttpStatus.UNAUTHORIZED)


        if (!seller.comparePassword(body.password.toString())) {
//            return ResponseEntity.badRequest().body(Message("Invalid password!"))

            return ResponseEntity(Message("Invalid password!"), HttpStatus.UNAUTHORIZED)
        }
//        val issuer = seller._id.toString()
        val issuer = seller._id.toString()
        //Old code to create token
//        try {
//            val jwt = Jwts.builder()
//                .setIssuer(issuer)
//                .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
//                .signWith(SignatureAlgorithm.HS512, seller.email).compact()
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
        resp["_id"] = seller._id.toString()
        resp["name"] = seller.name
        resp["email"] = seller.email
        resp["mobileNo"] = seller.mobileNo
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