package com.anchoreer.chat.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler(), "/chat").setAllowedOrigins("*")
    }

    @Bean
    fun chatHandler(): WebSocketHandler {
        return ChatWebSocketHandler()
    }
}

class ChatWebSocketHandler : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        // 메시지 처리 로직
        session.sendMessage(TextMessage("Message received: $payload"))
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        // 클라이언트가 연결되었을 때 처리
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        // 클라이언트가 연결을 닫았을 때 처리
    }
}
