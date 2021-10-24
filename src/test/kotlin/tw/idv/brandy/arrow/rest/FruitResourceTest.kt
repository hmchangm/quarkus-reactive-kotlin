package tw.idv.brandy.arrow.rest

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
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
        given().`when`()
            .body("""{"name" : "Kiwi","desc":"New Zealand fruit"}""")
            .contentType("application/json")
            .post("/fruits")
            .then()
            .statusCode(201)
            .body(containsString(""""id":"""), containsString(""""name":"Kiwi""""))

        given().`when`()
            .body("""{"name" : "Durian","desc":"Malaysia fruit"}""")
            .contentType("application/json")
            .post("/fruits")
            .then()
            .statusCode(201)
            .body(containsString(""""id":"""), containsString(""""name":"Durian""""))

         given().`when`()
            .get("/fruits")
            .then()
            .statusCode(200)
            .body(
                containsString("Kiwi"),
                containsString("Durian")
            )



    }
}
