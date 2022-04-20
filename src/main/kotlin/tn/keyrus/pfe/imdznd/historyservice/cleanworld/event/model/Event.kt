package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model

import io.vavr.control.Either
import java.time.LocalDateTime
import java.util.*

class Event private constructor(
    val action: EventAction,
    val objectId: String,
    val eventTime: LocalDateTime
) {

    companion object Builder {

        fun of(
            action: EventAction,
            objectId: String,
            eventTime: LocalDateTime,
        ) =
            checkEvent(
                objectId,
                eventTime
            )
                .let {
                    checkErrors(
                        it,
                        action,
                        objectId,
                        eventTime
                    )
                }

        private fun checkErrors(
            eventErrors: Sequence<EventError> = emptySequence(),
            action: EventAction,
            objectId: String,
            eventTime: LocalDateTime
        ): Either<Sequence<EventError>, Event> =
            if (eventErrors.count() == 0)
                Either.right(
                    Event(
                        action,
                        objectId,
                        eventTime
                    )
                )
            else
                Either.left(eventErrors)

        private fun checkEvent(
            objectId: String,
            eventTime: LocalDateTime
        ) =
            sequenceOf(
                checkObjectId(objectId),
                checkEventTime(eventTime),
            )
                .filter { it.isPresent }
                .map { it.get() }

        private fun checkEventTime(eventTime: LocalDateTime) =
            checkField(
                eventTime,
                EventError.EventTimeError
            ) { it.isAfter(LocalDateTime.now()).not() }

        private fun checkObjectId(objectId: String) =
            checkField(
                objectId,
                EventError.EventObjectIdError,
                String::isNotEmpty
            )

        private fun <T> checkField(
            field: T,
            error: EventError,
            validCondition: (T) -> Boolean
        ) =
            if (validCondition(field))
                Optional.empty()
            else
                Optional.of(error)

    }

    enum class EventAction {
        SAVEUSER,
        UPDATEUSER,
        DELETEUSER,
        FLAGUSER,
        FLAGTRANSACTION
    }

    sealed interface EventError {
        object EventObjectIdError : EventError
        object EventTimeError : EventError
    }

}