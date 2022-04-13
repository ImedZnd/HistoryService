package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.queue

import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import java.time.LocalDateTime

class EventQueueHandler(
    @Autowired private val eventService: EventService
) {
    suspend fun flagPersonHandler(objectId:String) =
        createEvent(Event.EventAction.FLAGUSER,objectId)

    suspend fun savePersonHandler(objectId:String) =
        createEvent(Event.EventAction.SAVEUSER,objectId)

    suspend fun updatePersonHandler(objectId:String) =
        createEvent(Event.EventAction.UPDATEUSER,objectId)

    suspend fun deletePersonHandler(objectId:String) =
        createEvent(Event.EventAction.DELETEUSER,objectId)

    suspend fun flagTransactionHandler(objectId:String) =
        createEvent(Event.EventAction.FLAGTRANSACTION,objectId)

    private suspend fun createEvent(action:Event.EventAction, objectId:String): Either<EventService.EventServiceIOError, Event> =
        eventService.saveEvent(
            Event.of(
                action,
                objectId,
                LocalDateTime.now()
            ).get()
        )
}