package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.context.MessageSource
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO.Builder.toEventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date.Companion.toDate
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.DateRange
import java.time.LocalDateTime
import java.util.*

class EventRestHandler(
    private val eventService: EventService,
    private val messageSource: MessageSource
) {

    suspend fun getAllEvents() =
        ok()
            .bodyAndAwait(
                eventService.getAllEvents()
                    .map { it.toEventDTO() }
            )

    suspend fun getAllEventsByAction(request: ServerRequest) =
        try {
            ok()
                .bodyAndAwait(
                    eventService.getAllEventByAction(
                        Event.EventAction.valueOf(
                            request.pathVariable("action").uppercase()
                        )
                    )
                        .map { it.toEventDTO() }
                )
        } catch (exception: IllegalArgumentException) {
            badRequest()
                .header(
                    "error",
                    headerErrorInBadRequestError("BadEventAction")
                )
                .buildAndAwait()
        }

    suspend fun getAllEventsByObjectId(request: ServerRequest) =
        ok()
            .bodyAndAwait(
                eventService.getAllEventByObjectId(
                    request.pathVariable("objectId")
                )
                    .map { it.toEventDTO() }
            )

    suspend fun getAllEventsByBetweenRange(request: ServerRequest) =
        request.awaitBody<DateRange>()
            .let { dateRange ->
                val dateRangeCheck = dateRange.checkStartDateAndEndDate()
                if (dateRangeCheck.isPresent)
                    dateRangeCheck.get().badRequestError()
                else
                    okResponse(dateRange.startDate, dateRange.endDate)
            }

    suspend fun getAllEventsBySpecifiedDate(request: ServerRequest) =
        request.awaitBody<Date>()
            .toLocalDateTime()
            .let { eventOrErrorOfEitherDate(it) }

    private suspend fun eventOrErrorOfEitherDate(either: Either<Date.DateError, LocalDateTime>) =
        if (either.isRight)
            either.get()
                .toDate()
                .let { okResponse(it) }
        else
            either.left.badRequestError()

    private suspend fun DateRange.DateRangeError.badRequestError(): ServerResponse {
        return badRequest()
            .header(
                "error",
                headerErrorInBadRequestError("EndDateBeforeStartDateError")
            )
            .buildAndAwait()
    }

    private suspend fun Date.DateError.badRequestError(): ServerResponse {
        return badRequest()
            .header(
                "error",
                headerErrorInBadRequestError("DateError")
            )
            .buildAndAwait()
    }

    private fun headerErrorInBadRequestError(string: String) =
        messageSource.getMessage(string, null, Locale.US)

    private suspend fun okResponse(startDate: Date, endDate: Date = startDate) =
        ok()
            .bodyAndAwait(
                getAllEventsInRange(startDate, endDate)
            )

    private fun getAllEventsInRange(startDate: Date, endDate: Date = startDate): Flow<EventDTO> {
        return eventService.getAllEventBetweenRange(
            startDate.toLocalDateTime().get().toLocalDate(),
            endDate.toLocalDateTime().get().toLocalDate()
        )
            .map{ it.toEventDTO() }
    }
}