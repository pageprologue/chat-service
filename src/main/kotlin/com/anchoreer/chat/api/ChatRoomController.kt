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

    @GetMapping("/{roomId}")
    fun findAllChatRoom(@PathVariable roomId: String): ChatRest.Res {
        return chatRoomService.findChatRoom(roomId)
    }

    @PutMapping("/{roomId}/users")
    fun addUserToRoom(
        @PathVariable roomId: String,
        @RequestBody users: List<UUID>
    ): ChatRest.Res {
        return chatRoomService.addUserToRoom(roomId, users)
    }

    @DeleteMapping("/{roomId}")
    fun removeUserFromRoom(
        @PathVariable roomId: String,
        @RequestParam("userUuid") userUuid: UUID
    ): ChatRest.Res {
        return chatRoomService.removeUserFromRoom(roomId, userUuid)
    }
}
