package com.anchoreer.user.service

import com.anchoreer.user.model.User
import com.anchoreer.user.model.repoitory.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun createUser(username: String): User {
        val user = User(uuid = UUID.randomUUID(), username = username)
        return userRepository.save(user)
    }

    fun findUsers(): List<User> {
        return userRepository.findAll()
    }

    fun findUserByUuid(uuid: UUID): User? {
        return userRepository.findById(uuid).orElse(null)
    }
}