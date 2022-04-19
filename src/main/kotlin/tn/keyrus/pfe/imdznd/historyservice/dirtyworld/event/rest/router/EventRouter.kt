package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventHandler

@Configuration
class EventRouter() {

    @Bean
    fun routes(eventHandler: EventHandler) = coRouter {
        "/events".nest {
            GET("") { eventHandler.getAllEvents() }
            GET("/action/{action}", eventHandler::getAllEventsByAction)
            GET("/objectId/{objectId}", eventHandler::getAllEventsByObjectId)
            POST("/daterange", eventHandler::getAllEventsByBetweenRange)
            POST("/date",eventHandler::getAllEventsBySpecifiedDate)
        }
    }
}