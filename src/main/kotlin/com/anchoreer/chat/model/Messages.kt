package com.anchoreer.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "messages")
class Messages(
    @Id val uuid: UUID = UUID.randomUUID(),
    val roomUuid: UUID,
    val userEmail: String,
    val content: String,
    val timestamp: Date = Date()
)
