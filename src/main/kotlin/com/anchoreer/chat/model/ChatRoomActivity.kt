package com.anchoreer.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.util.*

@RedisHash("chatRoomActivity")
data class ChatRoomActivity(
    @Id val roomUuid: UUID,
    val lastActiveTime: Long = System.currentTimeMillis()
)
