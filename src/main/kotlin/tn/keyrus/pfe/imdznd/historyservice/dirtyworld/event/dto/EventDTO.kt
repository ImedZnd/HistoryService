package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto

import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date.Companion.toDate

data class EventDTO(
    val action: Event.EventAction,
    val objectId: String,
    val eventTime: Date
) {
    companion object {
        fun Event.toEventDTO() =
            EventDTO(
                action,
                objectId,
                eventTime.toDate()
            )
    }
}