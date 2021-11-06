package tw.idv.brandy.arrow.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        Given {
            log().method()
        } When {
            get("/greeting")
        } Then {
            statusCode(200)
            body("message", equalTo("hello"))
        }
    }

}