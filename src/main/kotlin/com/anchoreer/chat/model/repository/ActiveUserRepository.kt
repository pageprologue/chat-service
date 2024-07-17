package com.anchoreer.chat.model.repository

import com.anchoreer.chat.model.ActiveUser
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ActiveUserRepository : CrudRepository<ActiveUser, UUID>
