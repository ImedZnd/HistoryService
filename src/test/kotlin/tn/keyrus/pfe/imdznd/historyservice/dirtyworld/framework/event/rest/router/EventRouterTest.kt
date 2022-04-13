package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.rest.router

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto.EventDTO
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.rest.handler.EventHandler
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer
import java.time.LocalDate
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
        runBlocking{
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
        runBlocking{
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
        runBlocking{
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
        runBlocking{
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
        runBlocking{
            val date = LocalDate.now()
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/date/$date")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(1)
        }
    }

    @Test
    fun `bad request on specific bad format date `() {
        runBlocking{
            val date = 2020/20/50
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/date/$date")
                .exchange()
                .expectStatus()
                .isBadRequest
                .expectBodyList<EventHandler.BadDatesInRequest>()
                .hasSize(1)
        }
    }

    @Test
    fun `two element on date range `() {
        runBlocking{
            val startDate = LocalDate.now().minusDays(5)
            val endDate = LocalDate.now()
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now().minusDays(3)
                ).get()
            )
            eventService.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "mmm",
                    LocalDateTime.now().minusDays(3)
                ).get()
            )
            webTestClient
                .get()
                .uri("/events/daterange/$startDate/$endDate")
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<EventDTO>()
                .hasSize(2)
        }
    }

    @Test
    fun `one element on object id `() {
        runBlocking{
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
        runBlocking{
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