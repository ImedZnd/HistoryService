package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.model

import io.vavr.control.Either
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime

@Data
@AllArgsConstructor
@NoArgsConstructor
class Date(
    private val day: Int,
    private val month: Int,
    private val year: Int
) {
    companion object Builder{
        fun stringToLocalDate(date: String): Either<DateError, LocalDate> {
            val dateAsString = date.split('-')
            return try {
                Either.right(
                    LocalDate.of(
                        dateAsString[0].toInt(),
                        dateAsString[1].toInt(),
                        dateAsString[2].toInt()
                    )
                )
            } catch (exception: Exception) {
                Either.left(DateError)
            }
        }
    }

//    fun toLocalDateTime(): Either<DateTimeException, LocalDateTime> {
//        return try {
//            Either.right(
//                LocalDateTime.of(
//                    year,
//                    month,
//                    day,
//                    0,
//                    0,
//                    0
//                )
//            )
//        } catch (dateTimeException: DateTimeException) {
//            Either.left(dateTimeException)
//        }
//    }

    object DateError
}