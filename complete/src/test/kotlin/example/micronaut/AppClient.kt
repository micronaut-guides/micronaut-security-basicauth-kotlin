package example.micronaut

import io.micronaut.http.client.annotation.Client
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header

@Client("/")
interface AppClient {

    @Get("/")
    fun home(@Header authorization: String): String
}