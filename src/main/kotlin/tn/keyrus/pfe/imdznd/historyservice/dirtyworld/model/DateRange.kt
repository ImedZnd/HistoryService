package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import io.vavr.control.Either
import java.time.LocalDate

data class DateRange(
    val startDate:Date,
    val endDate:Date,
){

    fun checkStartDateAndEndDate():Either<DateRangeError,DateRange>{
        if (checkDateIsRight(startDate).isLeft or checkDateIsRight(endDate).isLeft or checkStartDateIsBeforeEndDate().not())
            return Either.left(DateRangeError)
        return Either.right(DateRange(startDate,endDate))
    }

    private fun checkDateIsRight(date: Date) =
        date.toLocalDate()

    private fun checkStartDateIsBeforeEndDate() =
        startDate.toLocalDate().get().isBefore(endDate.toLocalDate().get())

    object DateRangeError
}