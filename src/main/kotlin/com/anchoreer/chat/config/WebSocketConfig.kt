package com.anchoreer.chat.config

import com.anchoreer.chat.handler.ChatWebSocketHandler
import com.anchoreer.chat.service.ChatRoomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(private val chatRoomService: ChatRoomService) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler(), "/chat").setAllowedOrigins("*")
    }

    @Bean
    fun chatHandler(): WebSocketHandler {
        return ChatWebSocketHandler(chatRoomService)
    }
}
