package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@EnableRabbit
@ComponentScan("tn.keyrus.pfe.imdznd.historyservice.**")
@SpringBootApplication
@EnableR2dbcRepositories("tn.keyrus.pfe.imdznd.historyservice.**")
@ConfigurationPropertiesScan(basePackages = ["tn.keyrus.pfe.imdznd.historyservice.**"])
@OpenAPIDefinition(info = Info(title = "History Service", version = "1.0", description = "Documentation APIs v1.0"))
class HistoryServiceApplication

fun main(args: Array<String>) {
    runApplication<HistoryServiceApplication>(*args)
}
