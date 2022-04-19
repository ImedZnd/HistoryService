package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import io.vavr.control.Either
import java.time.LocalDate

data class DateRange(
    val startDate:Date,
    val endDate:Date,
){

    companion object Builder {
        fun toDateRange(localDate1: LocalDate, localDate2: LocalDate) {
            DateRange(Date(
                localDate1.dayOfMonth,
                localDate1.monthValue,
                localDate1.year,
            ),Date(
                localDate2.dayOfMonth,
                localDate2.monthValue,
                localDate2.year,
            )

            )
        }
    }
    fun checkStartDateAndEndDate():Either<DateRangeError,DateRange>{
        if (checkDateIsRight(startDate).isLeft or checkDateIsRight(endDate).isLeft or checkStartDateIsBeforeEndDate().not())
            return Either.left(DateRangeError)
        return Either.right(DateRange(startDate,endDate))
    }

    private fun checkDateIsRight(date: Date) =
        date.toLocalDate()

    private fun checkStartDateIsBeforeEndDate() =
        startDate.toLocalDate().get().isBefore(endDate.toLocalDate().get())

    private fun returnStartDate() =
        startDate.toLocalDate().get()

    private fun returnEndDate()=
        endDate.toLocalDate().get()

    object DateRangeError
}