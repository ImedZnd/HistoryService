package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.rest.router

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.reactive.function.BodyInserters
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventHandler
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.DateRange
import java.time.LocalDateTime

@SpringBootTest
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
internal class EventRouterTest(
    @Autowired private val reactiveDatabaseRepository: ReactiveDatabaseRepository,
    @Autowired private val webTestClient: WebTestClient,
    @Autowired private val eventService: EventService
) {
    @BeforeAll
    fun beforeAll() {
        reactiveDatabaseRepository.deleteAll().subscribe()
    }

    @BeforeEach
    fun beforeEach() {
        reactiveDatabaseRepository.deleteAll().subscribe()
    }

    @AfterEach
    fun afterEach() {
        reactiveDatabaseRepository.deleteAll().subscribe()
    }

    @AfterAll
    fun afterAll() {
        reactiveDatabaseRepository.deleteAll().subscribe()
    }

    @Test
    fun `empty list if empty repository on get all`() {
        runBlocking {
            webTestClient
                .get()
                .uri("/events")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(0)
        }
    }

    @Test
    fun `list of one if repository contains one element on get all`() {
        runBlocking {
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of one if repository contains one element on get by action upper case action`() {
        runBlocking {
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/action/SAVEUSER")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of one if repository contains one element on get by action lower case action`() {
        runBlocking {
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/action/saveuser")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `error if bad request in action `() {
        runBlocking {
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/action/ddd")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectBodyList<EventHandler.BadEventAction>()
                .hasSize(1)
        }
    }

    @Test
    fun `list of one ,single element with specified date `() {
        runBlocking {
            val date = Date(1,1,2022)
            val event =
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.of(2022,1,1,1,1,1)
                )
            println(event.isRight)
            eventService.saveEvent(
                event.get()
            )
            webTestClient
                .post()
                .uri("/events/date")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(date))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `bad request on specific bad format date `() {
        runBlocking {
            val date2 = Date(1,1,2022)
                eventService.saveEvent(
                    Event.of(
                        Event.EventAction.SAVEUSER,
                        "x",
                        LocalDateTime.of(2022,1,1,1,1)
                    ).get()
                )
            webTestClient
                .post()
                .uri("/events/date")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(date2))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `two element on date range `() {
        runBlocking {
            val date1 = Date(1,1,2017)
            val date2 = Date(1,1,2022)
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now().minusYears(1)
                ).get()
            )
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "mmm",
                    LocalDateTime.now().minusYears(1)
                ).get()
            )
            val dateVal = DateRange(date1,date2)
            webTestClient
                .post()
                .uri("/events/daterange")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dateVal))
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `one element on object id `() {
        runBlocking {
            val objectId = "objectId"
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    objectId,
                    LocalDateTime.now().minusDays(3)
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/objectId/$objectId")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `zero element on object id `() {
        runBlocking {
            webTestClient
                .get()
                .uri("/events/objectId/xx")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(0)
        }
    }
}