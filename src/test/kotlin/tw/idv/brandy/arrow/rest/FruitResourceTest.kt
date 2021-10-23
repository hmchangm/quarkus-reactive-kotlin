package tw.idv.brandy.arrow.rest

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import tw.idv.brandy.arrow.rest.testutil.PostgresResource


@QuarkusTest

 class FruitResourceTest {

    val postgresDb: PostgresResource = PostgresResource()

    @BeforeAll
    fun setup() {
        postgresDb.start()
    }

    @AfterAll
    fun cleanUp() {
        postgresDb.stop()
    }

    @Test
    fun testListAllFruits() {
        //List all, should have all 4 fruits the database has initially:
        given()
            .`when`().get("/fruits")
            .then()
            .statusCode(200)
            .body(
                containsString("Kiwi"),
                containsString("Durian"),
                containsString("Pomelo"),
                containsString("Lychee")
            )
        given()
            .`when`().get("/fruits/1")
            .then()
            .statusCode(200)
            .body(
                containsString("Kiwi")
            )

        given()
            .`when`().get("/fruits/6")
            .then()
            .statusCode(404)

        given()
            .`when`()
            .body("""{"name" : "Pear"}""")
            .contentType("application/json")
            .post("/fruits")
            .then()
            .statusCode(201)
            .body(
                containsString(""""id":"""),
                containsString(""""name":"Pear"""")
            )
    }



}