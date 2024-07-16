package com.anchoreer.user.api

import com.anchoreer.user.model.User
import com.anchoreer.user.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestParam username: String): User {
        return userService.createUser(username)
    }

    @GetMapping
    fun getUser(): List<User> {
        return userService.findUsers()
    }

    @GetMapping("/{uuid}")
    fun getUser(@PathVariable uuid: UUID): User? {
        return userService.findUserByUuid(uuid)
    }
}