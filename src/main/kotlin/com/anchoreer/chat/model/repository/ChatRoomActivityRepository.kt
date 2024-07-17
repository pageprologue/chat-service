package com.anchoreer.chat.model.repository

import com.anchoreer.chat.model.ChatRoomActivity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ChatRoomActivityRepository : CrudRepository<ChatRoomActivity, UUID>
