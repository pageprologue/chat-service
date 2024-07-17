package com.anchoreer.chat.model

import com.anchoreer.user.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "chat_rooms")
class ChatRoom(
    @Id val uuid: UUID = UUID.randomUUID(),
    val title: String,
    val createdAt: Date = Date(),
    val messages: MutableList<Messages> = mutableListOf(),
    var recentMessages: Messages? = null,
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

    fun send(messages: Messages) {
        this.messages.add(messages)
        this.recentMessages = this.messages.last()
    }
}
