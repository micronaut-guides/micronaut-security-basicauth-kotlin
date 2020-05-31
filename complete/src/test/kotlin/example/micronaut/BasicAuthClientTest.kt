package example.micronaut

import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

@MicronautTest // <1>
class BasicAuthClientTest {

    @Inject
    lateinit var appClient : AppClient // <2>

    @Test
    fun verifyBasicAuthWorks() {
        val credsEncoded = Base64.getEncoder().encodeToString("sherlock:password".toByteArray())
        val rsp = appClient.home("Basic $credsEncoded")
        Assertions.assertEquals(rsp, "sherlock")
    }
}