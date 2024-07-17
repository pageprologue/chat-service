package com.anchoreer.chat.service

import com.anchoreer.chat.api.rest.ChatRest
import com.anchoreer.chat.model.*
import com.anchoreer.chat.model.repository.*
import com.anchoreer.user.model.User
import com.anchoreer.user.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
class ChatRoomServiceTest {

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var chatRoomRepository: ChatRoomRepository

    @MockBean
    private lateinit var messagesRepository: MessagesRepository

    @MockBean
    private lateinit var chatRoomActivityRepository: ChatRoomActivityRepository

    @MockBean
    private lateinit var activeUserRepository: ActiveUserRepository

    private lateinit var chatRoomService: ChatRoomService

    @BeforeEach
    fun setUp() {
        chatRoomService = ChatRoomService(
            userService,
            chatRoomRepository,
            messagesRepository,
            chatRoomActivityRepository,
            activeUserRepository
        )
    }

    @Test
    fun `채팅방 생성`() {
        val userUuid = UUID.randomUUID()
        val user = User(userUuid, "dev@test.com")
        `when`(userService.findUserByUuid(userUuid)).thenReturn(user)
        `when`(chatRoomRepository.save(any(ChatRoom::class.java))).thenAnswer { it.arguments[0] }

        val response = chatRoomService.createChatRoom("러닝 크루", userUuid)

        assertEquals("러닝 크루", response.title)
        assertEquals(1, response.users.size)
        assertEquals("dev@test.com", response.users[userUuid])
    }

    @Test
    fun `채팅방 생성 예외 - 사용자 없음`() {
        val userUuid = UUID.randomUUID()
        `when`(userService.findUserByUuid(userUuid)).thenReturn(null)

        assertThrows(IllegalArgumentException::class.java) {
            chatRoomService.createChatRoom("러닝 크루", userUuid)
        }
    }

    @Test
    fun `채팅방에 초대`() {
        val roomUuid = UUID.randomUUID()
        val new = User(UUID.randomUUID(), "new@test.com")
        val chatRoom = ChatRoom(roomUuid, "러닝 크루", users = mutableListOf())
        `when`(userService.findUsersByUuidIn(listOf(new.uuid))).thenReturn((listOf(new)))
        `when`(chatRoomRepository.findChatRoomByUuid(roomUuid)).thenReturn(Optional.of(chatRoom))
        `when`(chatRoomRepository.save(any(ChatRoom::class.java))).thenAnswer { it.arguments[0] }

        val response = chatRoomService.addUserToRoom(roomUuid, listOf(new.uuid))

        assertEquals(1, response.users.size)
        assertEquals("new@test.com", response.users[new.uuid])
    }

    @Test
    fun `채팅방 나가기`() {
        val roomUuid = UUID.randomUUID()
        val new = User(UUID.randomUUID(), "aaa@test.com")
        val chatRoom = ChatRoom(roomUuid, "러닝 크루", users = mutableListOf())
        `when`(chatRoomRepository.findChatRoomByUuid(roomUuid)).thenReturn(Optional.of(chatRoom))
        `when`(chatRoomRepository.save(any(ChatRoom::class.java))).thenAnswer { it.arguments[0] }

        val response = chatRoomService.removeUserFromRoom(roomUuid, new.uuid)

        assertEquals(0, response.users.size)
    }

    @Test
    fun `메시지 보내기`() {
        val roomUuid = UUID.randomUUID()
        val userEmail = "test@test.com"
        val chatRoom = ChatRoom(roomUuid, "개발 스터디", messages = mutableListOf())
        `when`(chatRoomRepository.findChatRoomByUuid(roomUuid)).thenReturn(Optional.of(chatRoom))
        `when`(chatRoomRepository.save(any(ChatRoom::class.java))).thenAnswer { it.arguments[0] }
        `when`(messagesRepository.save(any(Messages::class.java))).thenAnswer { it.arguments[0] }

        val message = ChatRest.Message(roomUuid, userEmail, "안녕하세요.")
        val messages = chatRoomService.addMessage(message)

        assertEquals(1, messages.size)
        assertEquals("안녕하세요.", messages[0].content)
    }

    @Test
    fun `test getMessages`() {
        val roomUuid = UUID.randomUUID()
        val chatRoom = ChatRoom(
            roomUuid, "개발 스터디",
            messages = mutableListOf(
                Messages(
                    roomUuid = roomUuid, userEmail = "user1@test.com",
                    content = "ㅋㅋㅋㅋ", timestamp = Date(2024, 6, 18)
                ),
                Messages(
                    roomUuid = roomUuid, userEmail = "user2@test.com",
                    content = "ㅎㅎㅎㅎ", timestamp = Date(2024, 7, 18)
                ),
            )
        )
        `when`(chatRoomRepository.findChatRoomByUuid(roomUuid)).thenReturn(Optional.of(chatRoom))

        val messages = chatRoomService.getMessages(roomUuid)

        assertEquals(2, messages.size)
        assertEquals("ㅎㅎㅎㅎ", messages[0].content)
        assertEquals("ㅋㅋㅋㅋ", messages[1].content)
    }

    @Test
    fun `최근 사용자 수 집계`() {
        val roomUuid = UUID.randomUUID()
        val activeUser1 = ActiveUser(
            roomUuid = roomUuid,
            userEmail = "user1@test.com",
            lastActiveTime = System.currentTimeMillis()
        )
        val activeUser2 = ActiveUser(
            roomUuid = roomUuid,
            userEmail = "user2@test.com",
            lastActiveTime = System.currentTimeMillis()
        )
        `when`(activeUserRepository.findAll()).thenReturn(listOf(activeUser1, activeUser2))

        val count = chatRoomService.getRecentUserCount(roomUuid)

        assertEquals(2, count)
    }
}
