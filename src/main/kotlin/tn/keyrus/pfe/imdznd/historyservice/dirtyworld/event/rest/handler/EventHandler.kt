package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyAndAwait
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date

class EventHandler(
    @Autowired private val eventService: EventService
) {
    suspend fun getAllEvents() =
        ok()
            .bodyAndAwait(
                eventToEventDTO {
                    it.getAllEvents()
                }
            )

    suspend fun getAllEventsByAction(request: ServerRequest): ServerResponse {
        return try {
            ok()
                .bodyAndAwait(
                    eventToEventDTO {
                        it.getAllEventByAction(
                            Event.EventAction.valueOf(
                                extractPathVariable(request, "action").uppercase()
                            )
                        )
                    }
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
                eventToEventDTO {
                    it.getAllEventByObjectId(
                        extractPathVariable(request, "objectId")
                    )
                }
            )

    suspend fun getAllEventsByBetweenRange(request: ServerRequest): ServerResponse {
        val startDate = Date.stringToLocalDate(extractPathVariable(request, "startDate"))
        val endDate = Date.stringToLocalDate(extractPathVariable(request, "endDate"))
        if (startDate.isLeft or endDate.isLeft)
            return badRequest()
                .bodyAndAwait(
                    flowOf(BadDatesInRequest)
                )
        if (startDate.get().isAfter(endDate.get()))
            return badRequest()
                .bodyAndAwait(
                    flowOf(StartDayGreaterThanEndDay)
                )

        return ok()
            .bodyAndAwait(
                eventToEventDTO {
                    it.getAllEventBetweenRange(
                        startDate.get(),
                        endDate.get()
                    )
                }
            )
    }

    suspend fun getAllEventsBySpecifiedDate(request: ServerRequest): ServerResponse {
        val startDate = Date.stringToLocalDate(extractPathVariable(request, "date"))
        if (startDate.isLeft)
            return badRequest()
                .bodyAndAwait(
                    flowOf(BadDatesInRequest)
                )
        return ok()
            .bodyAndAwait(
                eventToEventDTO {
                    it.getAllEventBetweenRange(
                        startDate.get(),
                        startDate.get()
                    )
                }
            )
    }

    private fun eventToEventDTO(selections: (EventService) -> Flow<Event>): Flow<EventDTO> {
        return selections(eventService)
            .map { EventDTO.toEventDTO(it) }
    }

    private fun extractPathVariable(request: ServerRequest, parameter: String): String {
        return request.pathVariable(parameter)
    }

    object BadDatesInRequest
    object StartDayGreaterThanEndDay
    object BadEventAction
}