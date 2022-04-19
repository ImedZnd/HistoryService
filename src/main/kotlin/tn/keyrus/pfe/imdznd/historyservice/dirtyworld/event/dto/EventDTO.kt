package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto

import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date

data class EventDTO(
    val action: Event.EventAction,
    val objectId: String,
    val eventTime: Date
) {
    companion object Builder {
        fun Event.toEventDTO() =
            EventDTO(
                action,
                objectId,
                Date(
                    year = eventTime.year,
                    month = eventTime.monthValue,
                    day = eventTime.dayOfMonth
                )
            )
    }
}