package com.anchoreer.chat.api.rest

import java.util.*

interface ChatRest {

    data class Req(
        val user: UUID,
        val title: String,
    )
}
