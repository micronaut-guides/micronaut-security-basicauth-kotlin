package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.on
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BasicAuthClientSpec: Spek({
    describe("Verify HTTP Basic Auth works") {

        val embeddedServer : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java) // <1>
        on("Accessing a secured URL using a @Client which supplies basic auth in Authorization header") {
            val credsEncoded: String = Base64.getEncoder().encodeToString("sherlock:password".byteInputStream().readBytes())

            var exceptionThrown = false
            try {
                val appClient: AppClient = embeddedServer.applicationContext.getBean(AppClient::class.java) // <2>
                val rsp : String = appClient.home("Basic $credsEncoded") // <3>
                assertEquals(rsp, "sherlock")
            } catch(e: HttpClientResponseException) {  // <4>
                exceptionThrown = true
            }
            assertFalse(exceptionThrown)
        }
        afterGroup {
            embeddedServer.close()
        }
    }
})