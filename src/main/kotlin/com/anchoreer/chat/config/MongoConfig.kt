package com.anchoreer.chat.config

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import org.bson.UuidRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class MongoConfig {

    @Value("\${spring.data.mongodb.uri}")
    private lateinit var mongoUri: String

    @Bean
    fun mongoDatabaseFactory(): MongoDatabaseFactory {
        val settings = MongoClientSettings.builder()
            .applyConnectionString(com.mongodb.ConnectionString(mongoUri))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()
        val mongoClient = MongoClients.create(settings)
        return SimpleMongoClientDatabaseFactory(mongoClient, "chat_app")
    }

    @Bean
    fun mongoTemplate(mongoDatabaseFactory: MongoDatabaseFactory): MongoTemplate {
        return MongoTemplate(mongoDatabaseFactory)
    }
}