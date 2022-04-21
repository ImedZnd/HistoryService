package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.event.queue.listener

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.*
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.service.EventService
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.repository.ReactiveDatabaseRepository
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(initializers = [Initializer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EventQueueListenerTest(
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val rabbitAdmin: RabbitAdmin,
    @Autowired private val eventService: EventService,
    @Autowired private val reactiveDatabaseRepository: ReactiveDatabaseRepository
){
    @BeforeAll
    fun beforeAll() {
        reactiveDatabaseRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @BeforeEach
    fun beforeEach() {
        reactiveDatabaseRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @AfterEach
    fun afterEach() {
        reactiveDatabaseRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @AfterAll
    fun afterAll() {
        reactiveDatabaseRepository.deleteAll().subscribe()
        rabbitAdmin.purgeQueue("flagpersonqueue")
        rabbitAdmin.purgeQueue("savepersonqueue")
        rabbitAdmin.purgeQueue("updatepersonqueue")
        rabbitAdmin.purgeQueue("deletepersonqueue")
    }

    @Test
    fun `flag user consumer `() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val userId = "userId"
                rabbitTemplate.convertAndSend(
                    "flagpersonexchange",
                    "flagpersonroutingkey",
                    userId
                )
                Thread.sleep(1000)
                val result = eventService.getAllEvents().count()
                val detailRes = eventService.getAllEventByObjectId(userId).first()
                assertAll(
                    {assert(result == 1)},
                    {assert(detailRes.objectId == userId)},
                    {assert(detailRes.action == Event.EventAction.FLAGUSER)}
                )
            }
        }
    }

    @Test
    fun `save user consumer`() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val userId = "userId"
                rabbitTemplate.convertAndSend(
                    "savepersonexchange",
                    "savepersonroutingkey",
                    userId
                )
                Thread.sleep(1000)
                val result = eventService.getAllEvents().count()
                val detailRes = eventService.getAllEventByObjectId(userId).first()
                assertAll(
                    {assert(result == 1)},
                    {assert(detailRes.objectId == userId)},
                    {assert(detailRes.action == Event.EventAction.SAVEUSER)}
                )
            }
        }
    }

    @Test
    fun `update user consumer`() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val userId = "userId"
                rabbitTemplate.convertAndSend(
                    "updatepersonexchange",
                    "updatepersonroutingkey",
                    userId
                )
                Thread.sleep(1000)
                val result = eventService.getAllEvents().count()
                val detailRes = eventService.getAllEventByObjectId(userId).first()
                assertAll(
                    {assert(result == 1)},
                    {assert(detailRes.objectId == userId)},
                    {assert(detailRes.action == Event.EventAction.UPDATEUSER)}
                )
            }
        }
    }

    @Test
    fun `delete user consumer`() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val userId = "userId"
                rabbitTemplate.convertAndSend(
                    "deletepersonexchange",
                    "deletepersonroutingkey",
                    userId
                )
                Thread.sleep(1000)
                val result = eventService.getAllEvents().count()
                val detailRes = eventService.getAllEventByObjectId(userId).first()
                assertAll(
                    {assert(result == 1)},
                    {assert(detailRes.objectId == userId)},
                    {assert(detailRes.action == Event.EventAction.DELETEUSER)}
                )
            }
        }
    }

    @Test
    fun `flag transaction consumer`() {
        runBlocking {
            withContext(Dispatchers.IO) {
                val userId = "userId"
                rabbitTemplate.convertAndSend(
                    "flagtransactionexchange",
                    "flagtransactionroutingkey",
                    userId
                )
                Thread.sleep(1000)
                val result = eventService.getAllEvents().count()
                val detailRes = eventService.getAllEventByObjectId(userId).first()
                assertAll(
                    {assert(result == 1)},
                    {assert(detailRes.objectId == userId)},
                    {assert(detailRes.action == Event.EventAction.FLAGTRANSACTION)}
                )
            }
        }
    }
}