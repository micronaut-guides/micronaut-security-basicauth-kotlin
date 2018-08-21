package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BasicAuthSpec: Spek({
    describe("Verify HTTP Basic Auth works") {

        var embeddedServer : EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java) // <1>
        var client : RxHttpClient = RxHttpClient.create(embeddedServer.url) // <2>
        it("Accessing a secured URL without authenticating") {
            var exceptionThrown = false
            try {
                val request = HttpRequest.GET<Any>("/")
                client.toBlocking().exchange(request, String::class.java) // <3>
            } catch(e: HttpClientResponseException) {  // <4>
                exceptionThrown = true
            }
            assertTrue(exceptionThrown)
        }
        it("If a secured URL is accessed with Basic Auth, it can be accessed") {
                val request = HttpRequest.GET<Any>("/").basicAuth("sherlock", "password")  // <5>
                val rsp : HttpResponse<String> = client.toBlocking().exchange(request, String::class.java)  // <6>

                assertEquals(rsp.status()!!, HttpStatus.OK)
                assertEquals(rsp.body()!!, "sherlock") // <7>
        }
        afterGroup {
            client.close()
            embeddedServer.close()
        }

    }
})