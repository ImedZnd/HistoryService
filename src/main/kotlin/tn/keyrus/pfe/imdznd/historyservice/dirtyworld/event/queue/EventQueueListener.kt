package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.event.queue

import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
class EventQueueListener(
    private val eventQueueHandler: EventQueueHandler
) {

    @Bean
    fun flagPersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.flagPersonHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun savePersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.savePersonHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun updatePersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.updatePersonHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun deletePersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.deletePersonHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun flagTransactionQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.flagTransactionHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun fraudPersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.fraudPersonHandler(
                    message.payload
                )
            }
        }

    @Bean
    fun unFraudPersonQueueListener() =
        Consumer { message: Message<String> ->
            runBlocking {
                eventQueueHandler.unfraudPersonHandler(
                    message.payload
                )
            }
        }
}


