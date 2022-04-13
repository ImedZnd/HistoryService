package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dto

import io.vavr.control.Either
import lombok.AllArgsConstructor
import lombok.Data
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model.Date
import java.time.LocalDate

@Data
@AllArgsConstructor
class EventDTO(
    val action: Event.EventAction,
    val objectId: String,
    val eventTime: Date
) {
    companion object Builder{
        fun toEventDTO(event:Event): EventDTO {
             return EventDTO(
                 event.action,
                 event.objectId,
                 Date(
                     event.eventTime.year,
                     event.eventTime.monthValue,
                     event.eventTime.dayOfMonth
                 )
             )
        }
    }
}