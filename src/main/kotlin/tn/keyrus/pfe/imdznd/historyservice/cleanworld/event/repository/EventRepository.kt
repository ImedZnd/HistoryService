package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import java.time.LocalDate

interface EventRepository {

    fun findAllEvents(): Flow<Event>
    fun findAllEventByAction(eventAction: Event.EventAction): Flow<Event>
    fun findAllEventByObjectId(objectId: String): Flow<Event>
    fun findAllEventByDateRange(startDate:LocalDate,endDate: LocalDate = startDate): Flow<Event>
    suspend fun saveEvent(event:Event): Either<EventRepositoryIOError,Event>

    object EventRepositoryIOError
}