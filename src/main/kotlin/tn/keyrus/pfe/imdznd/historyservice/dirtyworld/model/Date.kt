package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import io.vavr.control.Either
import java.time.DateTimeException
import java.time.LocalDateTime

data class Date(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
) {

    fun toLocalDateTime(): Either<DateError, LocalDateTime> =
        try {
            Either.right(
                LocalDateTime.of(
                    year,
                    month,
                    dayOfMonth,
                    hour,
                    minute,
                    second
                )
            )
        } catch (dateTimeException: DateTimeException) {
            Either.left(DateError)
        }

    companion object {

        fun LocalDateTime.toDate() =
            Date(
                this.year,
                this.monthValue,
                this.dayOfMonth,
                this.hour,
                this.minute,
                this.second
            )

    }

    object DateError
}