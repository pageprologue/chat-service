package com.anchoreer.chat.service

import com.anchoreer.chat.api.rest.ChatRest
import com.anchoreer.chat.model.ActiveUser
import com.anchoreer.chat.model.ChatRoom
import com.anchoreer.chat.model.ChatRoomActivity
import com.anchoreer.chat.model.Messages
import com.anchoreer.chat.model.repository.ActiveUserRepository
import com.anchoreer.chat.model.repository.ChatRoomActivityRepository
import com.anchoreer.chat.model.repository.ChatRoomRepository
import com.anchoreer.chat.model.repository.MessagesRepository
import com.anchoreer.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatRoomService(
    private val userService: UserService,
    private val chatRoomRepository: ChatRoomRepository,
    private val messagesRepository: MessagesRepository,
    private val chatRoomActivityRepository: ChatRoomActivityRepository,
    private val activeUserRepository: ActiveUserRepository,
) {
    private val chatRooms = ConcurrentHashMap<String, MutableList<ChatRest.Message>>()

    @Transactional
    fun createChatRoom(title: String, userUuid: UUID): ChatRest.Res {
        val user = userService.findUserByUuid(userUuid) ?: throw IllegalArgumentException("User not found")
        val chatRoom = chatRoomRepository.save(ChatRoom(title, user))
        return convertChatRes(chatRoom)
    }

    fun findChatRoom(roomUuid: UUID): ChatRest.Res {
        return convertChatRes(getChatRoom(roomUuid))
    }

    @Transactional
    fun addUserToRoom(roomUuid: UUID, userUuid: List<UUID>): ChatRest.Res {
        val users = userService.findUsersByUuidIn(userUuid)

        val chatRoom = chatRoomRepository.save(
            getChatRoom(roomUuid).also {
                it.invite(users)
            }
        )

        users.forEach { user ->
            activeUserRepository.save(ActiveUser(roomUuid = roomUuid, userEmail = user.username))
        }
        chatRoomActivityRepository.save(ChatRoomActivity(roomUuid = roomUuid))

        return convertChatRes(chatRoom)
    }

    @Transactional
    fun removeUserFromRoom(roomUuid: UUID, userUuid: UUID): ChatRest.Res {
        val chatRoom = getChatRoom(roomUuid)
        chatRoom.leave(userUuid)
        activeUserRepository.deleteById(userUuid)

        return convertChatRes(chatRoomRepository.save(chatRoom))
    }

    @Transactional
    fun addMessage(req: ChatRest.Message): List<ChatRest.Message> {
        val chatRoom = getChatRoom(req.roomUuid).also {
            messagesRepository.save(
                Messages(
                    roomUuid = it.uuid,
                    userEmail = req.userEmail,
                    content = req.content,
                )
            ).apply {
                it.send(this)
            }
        }

        chatRoomRepository.save(chatRoom)

        chatRoomActivityRepository.save(ChatRoomActivity(roomUuid = chatRoom.uuid))
        activeUserRepository.save(ActiveUser(roomUuid = chatRoom.uuid, userEmail = req.userEmail))

        return chatRoom.messages.takeLast(30).map {
            ChatRest.Message(
                roomUuid = chatRoom.uuid,
                userEmail = it.userEmail,
                content = it.content
            )
        }
    }

    fun getMessages(roomUuid: UUID): List<ChatRest.Message> {
        val chatRoom = getChatRoom(roomUuid)
        val sortedMessages = chatRoom.messages.sortedByDescending { it.timestamp }

        return sortedMessages.takeLast(30).map {
            convertMessage(chatRoom, it)
        }
    }

    fun getRecentUserCount(roomUuid: UUID): Int {
        val thirtyMinutesAgo = System.currentTimeMillis() - 1800 * 1000
        val activeUsers = activeUserRepository.findAll()
            .filter { it.roomUuid == roomUuid }
        return activeUsers.count { it.lastActiveTime > thirtyMinutesAgo }
    }

    private fun getChatRoom(roomUuid: UUID): ChatRoom {
        return chatRoomRepository.findChatRoomByUuid(roomUuid)
            .orElseThrow { IllegalArgumentException("Chat room not found") }
    }

    private fun convertChatRes(chatRoom: ChatRoom) = ChatRest.Res(
        uuid = chatRoom.uuid,
        title = chatRoom.title,
        users = chatRoom.users.associate { it.uuid to it.username },
    )

    private fun convertMessage(
        chatRoom: ChatRoom,
        messages: Messages
    ): ChatRest.Message {
        return ChatRest.Message(roomUuid = chatRoom.uuid, userEmail = messages.userEmail, content = messages.content)
    }
}
