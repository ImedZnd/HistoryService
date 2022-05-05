package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.annotation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.Explode
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventRestHandler
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.DateRange

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@RouterOperations(
    RouterOperation(
        path = "/events",
        method = [RequestMethod.GET],
        beanClass = EventRestHandler::class,
        beanMethod = "getAllEvents",
        operation = Operation(
            operationId = "getAllEvents",
            method = "GET",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all events by successfully.",
                    content = [Content(schema = Schema(implementation = Event::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/events/action/{action}",
        method = [RequestMethod.GET],
        beanClass = EventRestHandler::class,
        beanMethod = "getAllEventsByAction",
        operation = Operation(
            operationId = "getAllEventsByAction",
            method = "GET",
            parameters = [Parameter(
                name = "action",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all events by event action successfully.",
                    content = [Content(schema = Schema(implementation = Event::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Bad Event Action.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ]
        )
    ),
    RouterOperation(
        path = "/events/objectId/{objectId}",
        method = [RequestMethod.GET],
        beanClass = EventRestHandler::class,
        beanMethod = "getAllEventsByObjectId",
        operation = Operation(
            operationId = "getAllEventsByObjectId",
            method = "GET",
            parameters = [Parameter(
                name = "objectId",
                `in` = ParameterIn.PATH,
                style = ParameterStyle.SIMPLE,
                explode = Explode.FALSE,
                required = true
            )],
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all events by object id successfully.",
                    content = [Content(schema = Schema(implementation = Event::class))]
                )
            ]
        )
    ),
    RouterOperation(
        path = "/events/daterange",
        method = [RequestMethod.POST],
        beanClass = EventRestHandler::class,
        beanMethod = "getAllEventsByBetweenRange",
        operation = Operation(
            operationId = "getAllEventsByBetweenRange",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all events by in range successfully.",
                    content = [Content(schema = Schema(implementation = Event::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Date Range Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                ),
            ],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = DateRange::class
                    )
                )]
            )
        )
    ),
    RouterOperation(
        path = "/events/date",
        method = arrayOf(RequestMethod.POST),
        beanClass = EventRestHandler::class,
        beanMethod = "getAllEventsBySpecifiedDate",
        operation = Operation(
            operationId = "getAllEventsByDate",
            method = "POST",
            responses = [
                ApiResponse(
                    responseCode = "200",
                    description = "get all events by in range successfully.",
                    content = [Content(schema = Schema(implementation = Event::class))]
                ),
                ApiResponse(
                    responseCode = "400",
                    description = "Date Error.",
                    content = [Content(schema = Schema(implementation = String::class))]
                )],
            requestBody = RequestBody(
                content = [Content(
                    schema = Schema(
                        implementation = Date::class
                    )
                )]
            )
        )
    ),
)

annotation class HistoryRouterInfo
