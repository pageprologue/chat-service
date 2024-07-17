package com.anchoreer.chat.model.repository

import com.anchoreer.chat.model.ChatRoom
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional
import java.util.UUID

interface ChatRoomRepository : MongoRepository<ChatRoom, String> {
    fun findChatRoomByUuid(uuid: UUID): Optional<ChatRoom>
}
