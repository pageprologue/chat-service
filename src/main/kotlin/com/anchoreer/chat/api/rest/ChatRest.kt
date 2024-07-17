package com.anchoreer.chat.api.rest

import java.util.*

interface ChatRest {

    data class Req(
        val user: UUID,
        val title: String,
    )

    data class Res(
        val uuid: UUID,
        val users: Map<UUID, String>,
        val title: String,
    )

    data class Message(
        val roomUuid: UUID,
        val userEmail: String,
        val content: String,
    )
}
