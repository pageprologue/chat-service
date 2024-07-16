package com.anchoreer.user.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
data class User(
    @Id val uuid: UUID = UUID.randomUUID(),
    val username: String,
    val joinedAt: Date = Date()
)