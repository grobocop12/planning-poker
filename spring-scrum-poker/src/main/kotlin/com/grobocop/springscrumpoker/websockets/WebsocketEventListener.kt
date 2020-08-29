package com.grobocop.springscrumpoker.websockets

import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebsocketEventListener {
    val logger = LoggerFactory.getLogger(WebsocketEventListener::class.java)

    @EventListener
    fun handleSessionConnectedEvent(event: SessionConnectedEvent) {
        logger.info("New connection")
    }

    @EventListener
    fun handleSessionDisconnectEvent(event: SessionDisconnectEvent) {
        logger.info("Disconnection")
    }
}