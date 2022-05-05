package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao.EventDAO
import java.time.LocalDateTime

interface ReactiveDatabaseRepository : ReactiveCrudRepository<EventDAO, Long> {
    fun findAllByAction(eventAction: Event.EventAction): Flux<EventDAO>
    fun findAllByObjectId(objectId:String): Flux<EventDAO>
    fun findAllByEventTimeBetween(startDate: LocalDateTime, endDate: LocalDateTime): Flux<EventDAO>
}