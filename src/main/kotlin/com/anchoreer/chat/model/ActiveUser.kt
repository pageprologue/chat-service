package com.anchoreer.chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.*

@RedisHash("activeUser")
data class ActiveUser(
    val roomUuid: UUID,
    @Id val userEmail: String,
    val lastActiveTime: Long = System.currentTimeMillis(),
    @TimeToLive val expiration: Long = 1800
)
