package com.anchoreer.chat.handler

import com.anchoreer.chat.api.rest.ChatRest
import com.anchoreer.chat.service.ChatRoomService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*

@Component
class ChatWebSocketHandler(
    private val chatRoomService: ChatRoomService
) : TextWebSocketHandler() {

    val sessions = mutableMapOf<String, MutableList<WebSocketSession>>()
    private val objectMapper = jacksonObjectMapper()

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val chatMessage: ChatRest.Message = objectMapper.readValue(payload)
        chatRoomService.addMessage(chatMessage)

        sessions[chatMessage.roomUuid.toString()]?.forEach {
            if (it.isOpen) {
                it.sendMessage(TextMessage(objectMapper.writeValueAsString(chatMessage)))
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "Connection established ... (sessionId: ${session.id})" }
        val roomUuid = session.uri?.query?.split("=")?.get(1) ?: return
        if (sessions[roomUuid] == null) {
            sessions[roomUuid] = mutableListOf()
        }
        sessions[roomUuid]?.add(session)
        chatRoomService.addUserToRoom(UUID.fromString(roomUuid), listOf(UUID.fromString(session.id)))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info { "Connection closed ... (sessionId: ${session.id}, status: $status)" }
        val roomUuid = session.uri?.query?.split("=")?.get(1) ?: return
        sessions[roomUuid]?.remove(session)
        chatRoomService.removeUserFromRoom(UUID.fromString(roomUuid), UUID.fromString(session.id))
    }

    companion object : KLogging()
}
