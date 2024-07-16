package com.anchoreer.chat.api

import com.anchoreer.chat.api.rest.ChatRest
import com.anchoreer.chat.model.ChatRoom
import com.anchoreer.chat.service.ChatRoomService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat/rooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {

    @PostMapping
    fun createChatRoom(@RequestBody req: ChatRest.Req): ChatRoom {
        return chatRoomService.createChatRoom(
            title = req.title,
            userUuid = req.user,
        )
    }
}