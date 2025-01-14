package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.repository

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.DatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class DatabaseDatabaseRepositoryTest(
    @Autowired private val databaseRepository: DatabaseRepository,
    @Autowired private val reactiveDatabaseRepository: ReactiveDatabaseRepository
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
    fun `find all return one element on save operation if repository is have one element`() {
        runBlocking {
            generateSequence(1) { it + 1 }
                .take(1)
                .map {
                    Event.of(
                        Event.EventAction.SAVEUSER,
                        "x",
                        LocalDateTime.now()
                    )
                }
                .filter { it.isRight }
                .map { it.get() }
                .forEach { databaseRepository.saveEvent(it) }

            val result =
                databaseRepository
                    .findAllEvents()
                    .count()

            assert(result == 1)
        }
    }

    @Test
    fun `no element on find all operation if repository is empty`() {
        runBlocking {
            val result =
                databaseRepository
                    .findAllEvents()
                    .count()

            assert(result == 0)
        }
    }

    @Test
    fun `find all and find all by action retrun one element on findAllByAction operation if repository is has one element with that Action`() {
        runBlocking {
            databaseRepository.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    LocalDateTime.now()
                ).get()
            )
            val resultAll =
                databaseRepository
                    .findAllEvents()
                    .count()
            val resultSpecific =
                databaseRepository
                    .findAllEventByAction(Event.EventAction.SAVEUSER)
                    .count()

            assertAll(
                { assert(resultAll == 1) },
                { assert(resultSpecific == 1) },
            )
        }
    }

    @Test
    fun `find all and find all by Object id one element on findAllByType operation if repository is has one element with that ObjectId`() {
        runBlocking {
            val objectId = "x"
            databaseRepository.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    objectId,
                    LocalDateTime.now()
                ).get()
            )
            val resultAll =
                databaseRepository
                    .findAllEvents()
                    .count()
            val resultSpecific =
                databaseRepository
                    .findAllEventByObjectId(objectId)
                    .count()

            assertAll(
                { assert(resultAll == 1) },
                { assert(resultSpecific == 1) },
            )
        }
    }

    @Test
    fun `find all and find all by time in range one element on findAllByTimeRange operation if repository has one element with that date`() {
        runBlocking {
            val testDate = LocalDateTime.now().minusDays(1)
            databaseRepository.saveEvent(
                Event.of(
                    Event.EventAction.SAVEUSER,
                    "x",
                    testDate
                ).get()
            )
            val resultAll =
                databaseRepository
                    .findAllEvents()
                    .count()
            val resultSpecific =
                databaseRepository
                    .findAllEventByDateRange(
                        LocalDateTime.now().minusDays(1).toLocalDate(),
                        LocalDateTime.now().minusDays(1).toLocalDate()
                    ).count()

            assertAll(
                { assert(resultAll == 1) },
                { assert(resultSpecific == 1) },
            )
        }
    }
}