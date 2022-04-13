package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.dao

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import tn.keyrus.pfe.imdznd.historyservice.cleanworld.event.model.Event
import java.time.LocalDateTime

@Table("event")
data class EventDAO(
    @Id
    val id: Long? = null,
    val action: Event.EventAction = Event.EventAction.SAVEUSER,
    val objectId: String = "",
    val eventTime: LocalDateTime = LocalDateTime.now()
) {

    fun toEvent() =
        Event.of(
            this.action,
            this.objectId,
            this.eventTime
        )

    companion object {
        fun Event.toDAO() =
            EventDAO(
                action = action,
                objectId = objectId,
                eventTime = eventTime
            )
    }

}
