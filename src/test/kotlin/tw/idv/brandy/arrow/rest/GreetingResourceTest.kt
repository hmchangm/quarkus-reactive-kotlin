package tw.idv.brandy.arrow.rest

import io.quarkus.test.junit.QuarkusTest

import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        Given("desc"){
            contentType("application/json")
        } When {
            get("/greeting")
        } Then {
            statusCode(200)
            body("message", equalTo("hello"))
        }
    }

}