package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.queue.EventQueueHandler
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.DatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventRestHandler

@Configuration
class EventConfiguration {

    @Bean
    fun databaseRepository(
        reactiveDatabaseRepository: ReactiveDatabaseRepository
    ): EventRepository =
        DatabaseRepository(reactiveDatabaseRepository)

    @Bean
    fun eventService(
        eventRepository: EventRepository
    ) =
        EventService(eventRepository)

    @Bean
    fun eventHandler(
        eventService: EventService
    ) =
        EventRestHandler(eventService)

    @Bean
    fun eventQueueHandler(
        eventService: EventService
    ) =
        EventQueueHandler(eventService)

}