package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import java.util.*

data class DateRange(
    val startDate: Date,
    val endDate: Date = startDate,
) {
    fun checkStartDateAndEndDate() =
        when {
            this.startDate.toLocalDateTime().isLeft or this.endDate.toLocalDateTime().isLeft ->
                Optional.of(DateRangeError.DateIsNotValidError)
            this.startDate.toLocalDateTime().get()!!.isAfter(this.endDate.toLocalDateTime().get()!!) ->
                Optional.of(DateRangeError.EndDateBeforeStartDateError)
            else -> Optional.empty()
        }

    sealed interface DateRangeError {
        object DateIsNotValidError : DateRangeError
        object EndDateBeforeStartDateError : DateRangeError
    }
}