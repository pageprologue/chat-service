package com.anchoreer.chat.api

import com.anchoreer.chat.api.rest.ChatRest
import com.anchoreer.chat.service.ChatRoomService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/chat/rooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService,
) {

    @PostMapping
    fun createChatRoom(@RequestBody req: ChatRest.Req): ChatRest.Res {
        return chatRoomService.createChatRoom(
            title = req.title,
            userUuid = req.user,
        )
    }

    @GetMapping("/{roomUuid}")
    fun findAllChatRoom(@PathVariable roomUuid: UUID): ChatRest.Res {
        return chatRoomService.findChatRoom(roomUuid)
    }

    @PutMapping("/{roomUuid}/users")
    fun addUserToRoom(
        @PathVariable roomUuid: UUID,
        @RequestBody users: List<UUID>
    ): ChatRest.Res {
        return chatRoomService.addUserToRoom(roomUuid, users)
    }

    @DeleteMapping("/{roomUuid}")
    fun removeUserFromRoom(
        @PathVariable roomUuid: UUID,
        @RequestParam("userUuid") userUuid: UUID
    ): ChatRest.Res {
        return chatRoomService.removeUserFromRoom(roomUuid, userUuid)
    }

    @PostMapping("/{roomUuid}/messages")
    fun addMessage(
        @PathVariable roomUuid: UUID,
        @RequestBody req: ChatRest.Message
    ) {
        chatRoomService.addMessage(req)
    }

    @GetMapping("/{roomUuid}/messages")
    fun getMessages(@PathVariable roomUuid: UUID): List<ChatRest.Message> {
        return chatRoomService.getMessages(roomUuid)
    }
}
