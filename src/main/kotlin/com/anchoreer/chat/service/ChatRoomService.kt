package com.anchoreer.chat.service

import com.anchoreer.chat.api.rest.ChatRest
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
    fun createChatRoom(title: String, userUuid: UUID): ChatRest.Res {
        val user = userService.findUserByUuid(userUuid) ?: throw IllegalArgumentException("User not found")
        val chatRoom = chatRoomRepository.save(ChatRoom(title, user))
        return convertChatRes(chatRoom)
    }

    fun findChatRoom(roomId: String): ChatRest.Res {
        return convertChatRes(getChatRoom(roomId))
    }

    @Transactional
    fun addUserToRoom(roomId: String, userUuid: List<UUID>): ChatRest.Res {
        val users = userService.findUsersByUuidIn(userUuid)

        val chatRoom = chatRoomRepository.save(getChatRoom(roomId).also {
            it.invite(users)
        })

        return convertChatRes(chatRoom)
    }

    @Transactional
    fun removeUserFromRoom(roomId: String, userUuid: UUID): ChatRest.Res {
        val chatRoom = getChatRoom(roomId)
        chatRoom.leave(userUuid)
        return convertChatRes(chatRoomRepository.save(chatRoom))
    }

    private fun getChatRoom(roomId: String): ChatRoom {
        return chatRoomRepository.findById(roomId).orElseThrow { IllegalArgumentException("Chat room not found") }
    }

    private fun convertChatRes(chatRoom: ChatRoom) = ChatRest.Res(
        id = chatRoom.id,
        title = chatRoom.title,
        users = chatRoom.users.associate { it.uuid to it.username },
    )

}
