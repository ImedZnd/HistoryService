package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.configuration.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfiguration {
    @Bean
    fun rabbitAdmin(@Autowired rabbitTemplate: RabbitTemplate): RabbitAdmin {
        return RabbitAdmin(rabbitTemplate)
    }
}