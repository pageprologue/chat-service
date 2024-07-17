package com.anchoreer.chat.model.repository

import com.anchoreer.chat.model.Messages
import org.springframework.data.mongodb.repository.MongoRepository

interface MessagesRepository : MongoRepository<Messages, String>
