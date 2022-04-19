package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import io.vavr.control.Either
import java.time.DateTimeException
import java.time.LocalDate

data class Date(
    val day: Int,
    val month: Int,
    val year: Int
) {
    companion object Builder {
        fun LocalDate.toDTO() {
            Date(
                this.dayOfMonth,
                this.monthValue,
                this.year,
            )
        }
    }

    fun checkDate(): Either<DateError, Date> {
        print("is the date right : "+this.toLocalDate().isRight)
        print(this.toLocalDate())
        if (this.toLocalDate().get().isAfter(LocalDate.now()))
            return Either.left(DateError)
        return Either.right(this)
    }

    fun toLocalDate(): Either<DateError, LocalDate> =
        try {
            Either.right(
                LocalDate.of(
                    year,
                    month,
                    day
                )
            )
        } catch (dateTimeException: DateTimeException) {
            Either.left(DateError)
        }

    object DateError
}