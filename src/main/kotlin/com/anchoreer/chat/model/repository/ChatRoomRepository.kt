package com.anchoreer.chat.model.repository

import com.anchoreer.chat.model.ChatRoom
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRoomRepository : MongoRepository<ChatRoom, String>
