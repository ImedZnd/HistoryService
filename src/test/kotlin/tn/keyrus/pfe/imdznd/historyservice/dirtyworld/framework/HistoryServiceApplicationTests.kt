package tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import tn.keyrus.pfe.imdznd.historyservice.dirtyworld.framework.initializer.Initializer

@SpringBootTest
@ContextConfiguration(initializers = [Initializer::class])
class HistoryServiceApplicationTests {

    @Test
    fun contextLoads() {
    }

}
