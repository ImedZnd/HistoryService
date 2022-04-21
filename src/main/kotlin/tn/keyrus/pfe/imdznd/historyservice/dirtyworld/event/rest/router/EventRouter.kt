package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventRestHandler

@Configuration
class EventRouter() {

    @Bean
    fun routes(eventRestHandler: EventRestHandler) = coRouter {
        "/events".nest {
            GET("") { eventRestHandler.getAllEvents() }
            GET("/action/{action}", eventRestHandler::getAllEventsByAction)
            GET("/objectId/{objectId}", eventRestHandler::getAllEventsByObjectId)
            POST("/daterange", eventRestHandler::getAllEventsByBetweenRange)
            POST("/date",eventRestHandler::getAllEventsBySpecifiedDate)
        }
    }
}