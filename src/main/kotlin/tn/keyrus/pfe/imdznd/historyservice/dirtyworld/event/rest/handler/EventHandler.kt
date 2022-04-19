package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler

import io.vavr.control.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO.Builder.toEventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.DateRange

class EventHandler(
    @Autowired private val eventService: EventService
) {
    suspend fun getAllEvents() =
        ok()
            .bodyAndAwait(
                eventService
                    .getAllEvents()
                    .map { it.toEventDTO() }
            )

    suspend fun getAllEventsByAction(request: ServerRequest): ServerResponse {
        return try {
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
                .bodyAndAwait(
                    flowOf(BadEventAction)
                )
        }
    }

    suspend fun getAllEventsByObjectId(request: ServerRequest): ServerResponse =
        ok()
            .bodyAndAwait(
                eventService.getAllEventByObjectId(
                    request.pathVariable("objectId")
                )
                    .map { it.toEventDTO() }
            )

    suspend fun getAllEventsByBetweenRange(request: ServerRequest) =
        request
            .bodyToMono(DateRange::class.java)
            .awaitSingle()
            .checkStartDateAndEndDate()
            .let { eventOrErrorOfEitherDateRange(it) }

    private suspend fun eventOrErrorOfEitherDateRange(either: Either<DateRange.DateRangeError, DateRange>): ServerResponse {
        if (either.isRight)
            return okResponse(either.get().startDate, either.get().endDate)
        return badRequestError(either.left)
    }

    suspend fun getAllEventsBySpecifiedDate(request: ServerRequest) =
        request
            .bodyToMono(Date::class.java)
            .awaitSingle()
            .checkDate()
            .let { eventOrErrorOfEitherDate(it) }

    private suspend fun eventOrErrorOfEitherDate(either: Either<Date.DateError, Date>): ServerResponse {
        print("is the either right : " + either.isRight)
        if (either.isRight)
            return okResponse(either.get(), either.get())
        return badRequestError(either.left)
    }


    private suspend fun badRequestError(it: Any): ServerResponse {
        return badRequest()
            .header("error", it.toString())
            .buildAndAwait()
    }

    private suspend fun okResponse(startDate: Date, endDate: Date): ServerResponse {
        return ok()
            .bodyAndAwait(
                getAllEventsInRange(startDate, endDate)
            )
    }

    private fun getAllEventsInRange(startDate: Date, endDate: Date): Flow<EventDTO> {
        return eventService.getAllEventBetweenRange(
            startDate.toLocalDate().get(),
            endDate.toLocalDate().get()
        ).map { it.toEventDTO() }
    }

    object BadEventAction
}