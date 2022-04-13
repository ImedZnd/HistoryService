package tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDateTime

internal class EventTest {

    @Test
    fun `valid test Event non empty field`() {
        val action = Event.EventAction.SAVEUSER
        val objectId = "objectId"
        val localtime = LocalDateTime.now()
        val result = Event.of(
            Event.EventAction.SAVEUSER,
            objectId,
            localtime,
        ).get()
        assertAll(
            { assert(result.action == action) },
            { assert(result.objectId == objectId) },
            { assert(result.eventTime == localtime) },
        )
    }

    @Test
    fun `Event ObjectID Empty Error`() {
        val action = Event.EventAction.SAVEUSER
        val objectId = ""
        val localtime = LocalDateTime.now()
        val result = Event.of(
            action,
            objectId,
            localtime
        ).left
        assert(result.count() == 1)
        assert(result.all { it is Event.EventError.EventObjectIdError })
    }

    @Test
    fun `event time error if time is after new`() {
        val action = Event.EventAction.SAVEUSER
        val objectId = "objectId"
        val localtime = LocalDateTime.now().plusYears(1)
        val result = Event.of(
            action,
            objectId,
            localtime
        ).left
        assertAll(
            { assert(result.count() == 1) },
            { assert(result.any { it is Event.EventError.EventTimeError }) }
        )
    }

    @Test
    fun `test Event 2 fields Empty Error`() {
        val count = 2
        val action = Event.EventAction.SAVEUSER
        val objectId = ""
        val localtime = LocalDateTime.now().plusYears(1)
        val result = Event.of(
            action,
            objectId,
            localtime
        ).left
        assertAll(
            { assert(result.count() == count) },
            { assert(result.any { it is Event.EventError.EventObjectIdError }) },
            { assert(result.any { it is Event.EventError.EventTimeError }) },
        )
    }


}