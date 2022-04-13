package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import reactor.core.publisher.Flux
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.repository.EventRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao.EventDAO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao.EventDAO.Companion.toDAO
import java.time.LocalDate

class DatabaseRepository (
    private val reactiveDatabaseRepository: ReactiveDatabaseRepository
) : EventRepository {

    override fun findAllEvents() =
        findAllEventsByCriteria { it.findAll() }

    override fun findAllEventByAction(eventAction: Event.EventAction) =
        findAllEventsByCriteria { it.findAllByAction(eventAction)}

    override fun findAllEventByObjectId(objectId: String) =
        findAllEventsByCriteria { it.findAllByObjectId(objectId)}

    override fun findAllEventByDateRange(startDate: LocalDate, endDate: LocalDate) =
        findAllEventsByCriteria { it.findAllByEventTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()) }

    override suspend fun saveEvent(event: Event): Either<EventRepository.EventRepositoryIOError, Event> =
        try {
            reactiveDatabaseRepository
                .save(event.toDAO())
                .map { it.toEvent() }
                .filter { it.isRight }
                .map { it.get() }
                .map { Either.right<EventRepository.EventRepositoryIOError, Event>(it) }
                .awaitSingleOrNull()
                ?: Either.left(EventRepository.EventRepositoryIOError)
        } catch (exception: Exception) {
            Either.left(EventRepository.EventRepositoryIOError)
        }

    private fun findAllEventsByCriteria(criteria: (ReactiveDatabaseRepository) -> Flux<EventDAO>): Flow<Event> =
        criteria(reactiveDatabaseRepository)
            .asFlow()
            .map { it.toEvent() }
            .filter { it.isRight }
            .map { it.get() }
}