package com.anchoreer.user.model.repoitory

import com.anchoreer.user.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User, UUID>
