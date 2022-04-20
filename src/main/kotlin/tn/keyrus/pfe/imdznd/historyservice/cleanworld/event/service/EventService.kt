package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service

import io.vavr.control.Either
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import java.time.LocalDate

class EventService(
    private val eventRepository: EventRepository
) {

    fun getAllEvents() =
        eventRepository.findAllEvents()

    fun getAllEventByAction(eventAction: Event.EventAction) =
        eventRepository.findAllEventByAction(eventAction)

    fun getAllEventByObjectId(objectId: String) =
        eventRepository.findAllEventByObjectId(objectId)

    fun getAllEventBetweenRange(startDate: LocalDate, endDate: LocalDate) =
        eventRepository.findAllEventByDateRange(startDate, endDate)

    suspend fun saveEvent(event: Event): Either<EventServiceIOError, Event> =
        eventRepository.saveEvent(event)
            .mapLeft { EventServiceIOError }

    object EventServiceIOError
}