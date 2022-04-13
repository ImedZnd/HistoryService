package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ApplicationListener
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) = runBlocking {
        val postgreSQLContainer =
            GenericContainer(DockerImageName.parse("postgres"))
                .withEnv("POSTGRES_USER", "postgres")
                .withEnv("POSTGRES_PASSWORD", "changeme")
                .withExposedPorts(5432)

        val rabbitMQContainer =
            GenericContainer(DockerImageName.parse("rabbitmq:management"))
                .withExposedPorts(5672, 15672)

        postgreSQLContainer.start()
        rabbitMQContainer.start()
        while (postgreSQLContainer.isRunning.not())
            delay(1000)
        while (rabbitMQContainer.isRunning.not())
            delay(1000)

        val postgresUrl = ("${postgreSQLContainer.host}:${postgreSQLContainer.getMappedPort(5432)}")

        TestPropertyValues
            .of(
                "spring.rabbitmq.host=" + rabbitMQContainer.host,
                "spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672),
                "spring.r2dbc.url=r2dbc:postgresql://$postgresUrl/postgres",
                "spring.r2dbc.username=postgres",
                "spring.r2dbc.password=changeme"
            )
            .applyTo(applicationContext.environment)

        val applicationContextCloseListener: ApplicationListener<ContextClosedEvent> =
            ApplicationListener<ContextClosedEvent> {
                runBlocking {
                    postgreSQLContainer.stop()
                    rabbitMQContainer.stop()
                    while (postgreSQLContainer.isRunning)
                        delay(1000)
                    while (rabbitMQContainer.isRunning)
                        delay(1000)
                }
            }

        applicationContext.addApplicationListener(applicationContextCloseListener)

    }
}
