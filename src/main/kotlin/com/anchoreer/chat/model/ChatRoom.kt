package com.anchoreer.chat.model

import com.anchoreer.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "chat_rooms")
class ChatRoom(
    @Id val id: String = "",
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

    fun invite(users: List<User>) {
        this.users.addAll(users)
    }

    fun leave(uuid: UUID) {
        users.removeIf { it.uuid == uuid }

    }
}

data class Message(
    val userUuid: UUID,
    val content: String,
    val timestamp: Date = Date()
)
