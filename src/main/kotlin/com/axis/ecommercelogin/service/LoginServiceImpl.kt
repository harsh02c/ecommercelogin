package com.axis.ecommercelogin.service

import com.axis.ecommercelogin.dao.ISellerDAO
import com.axis.ecommercelogin.dao.IUserDAO
import com.axis.ecommercelogin.model.User
import com.axis.ecommercelogin.model.Seller
import com.axis.ecommercelogin.service.ILoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginServiceImpl : ILoginService {
    @Autowired
    private lateinit var iUserDAO: IUserDAO
    @Autowired
    private lateinit var iSellerDAO: ISellerDAO

    override fun signUpUser(user: User): User? {

        return iUserDAO.save(user)
    }

    override fun signUpSeller(seller: Seller): Seller? {

        return iSellerDAO.save(seller)
    }

    override fun login(user: User): User? {

        return iUserDAO.save(user)
    }

    override fun findByEmailSeller(email: String): Seller? {
            return iSellerDAO.findByEmail(email)
    }

    override fun findByEmailUser(email: String): User? {
            return iUserDAO.findByEmail(email)
    }

    override fun getById(id: Int): Optional<User?> {
        return iUserDAO.findById(id)
    }


}