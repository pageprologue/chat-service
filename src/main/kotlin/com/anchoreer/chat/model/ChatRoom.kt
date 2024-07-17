package com.anchoreer.chat.model

import com.anchoreer.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "chat_rooms")
data class ChatRoom(
    @Id val id: String? = null,
    val title: String,
    val createdAt: Date = Date(),
    val messages: MutableList<Message> = mutableListOf(),
    var recentMessage: Message? = null,
    val users: MutableList<User> = mutableListOf()
) {
    constructor(title: String, user: User) : this(
        title = title,
        users = mutableListOf(user)
    )
}

data class Message(
    val userUuid: UUID,
    val content: String,
    val timestamp: Date = Date()
)
