package tw.idv.brandy.arrow.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.module.kotlin.extensions.*
import org.hamcrest.CoreMatchers.containsString
import org.jboss.logging.Logger
import org.junit.jupiter.api.Test

@QuarkusTestResource(MockMongoDatabase::class)
@QuarkusTest
class FruitResourceTest {

    companion object {
        private val LOG: Logger = Logger.getLogger(FruitResourceTest::class.java)
    }

    @Test
    fun testListAllFruits() {
        Given {
            body("""{"name" : "Kiwi","desc":"New Zealand fruit"}""")
            contentType("application/json")
        } When {
            post("/fruits")
        } Then {
            statusCode(201)
            body(containsString(""""id":"""), containsString(""""name":"Kiwi""""))
        }

        Given {
            body("""{"name" : "Durian","desc":"Malaysia fruit"}""")
                .contentType("application/json")
        } When {
            post("/fruits")
        } Then {
            statusCode(201)
            body(containsString(""""id":"""), containsString(""""name":"Durian""""))
        }
        Given {
            log().method()
        } When {

            get("/fruits")
        } Then {
            statusCode(200)
            body(
                containsString("Kiwi"),
                containsString("Durian")
            )
        }

        given().`when`()
            .get("/fruitsReact")
            .then()
            .statusCode(200)
            .body(
                containsString("Kiwi"),
                containsString("Durian")
            )


    }
}
