package com.anchoreer.chat.service

import com.anchoreer.chat.model.ChatRoom
import com.anchoreer.chat.model.repository.ChatRoomRepository
import com.anchoreer.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val userService: UserService,
) {

    @Transactional
    fun createChatRoom(title: String, userUuid: UUID): ChatRoom {
        val user = userService.findUserByUuid(userUuid) ?: throw IllegalArgumentException("User not found")
        val chatRoom = ChatRoom(title, user)
        return chatRoomRepository.save(chatRoom)
    }
}