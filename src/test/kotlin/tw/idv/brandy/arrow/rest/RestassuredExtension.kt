package tw.idv.brandy.arrow.rest

import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.given
import io.restassured.internal.ResponseSpecificationImpl
import io.restassured.internal.ValidatableResponseImpl
import io.restassured.response.ExtractableResponse
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import io.restassured.specification.RequestSender
import io.restassured.specification.RequestSpecification

fun Given(story:String,block: RequestSpecification.() -> RequestSpecification): RequestSpecification = given().run(block)

/**
 * A wrapper around [RequestSpecification.when] that configures how the request is dispatched.
 * @see RequestSpecification.when
 */
infix fun RequestSpecification.When(block: RequestSpecification.() -> Response): Response = `when`().run(block)

/**
 * A wrapper around [io.restassured.RestAssured.when] that configures how the request is dispatched.
 * @see io.restassured.RestAssured.when
 */
fun When(story:String,block: RequestSender.() -> Response): Response = `when`().run(block)

/**
 * A wrapper around [then] that allow configuration of response expectations.
 * @see then
 */
infix fun Response.Then(block: ValidatableResponse.() -> Unit): ValidatableResponse = then()
    .also(doIfValidatableResponseImpl {
        forceDisableEagerAssert()
    })
    .apply(block)
    .also(doIfValidatableResponseImpl {
        forceValidateResponse()
    })

/**
 * A wrapper around [ExtractableResponse] that allow for extract data out of the response
 * @see ExtractableResponse
 */
infix fun <T> Response.Extract(block: ExtractableResponse<Response>.() -> T): T = then().extract().run(block)

/**
 * A wrapper around [ExtractableResponse] that allow for extract data out of the response
 * @see ExtractableResponse
 */
infix fun <T> ValidatableResponse.Extract(block: ExtractableResponse<Response>.() -> T): T = extract().run(block)

// End main wrappers

private fun doIfValidatableResponseImpl(fn: ResponseSpecificationImpl.() -> Unit): (ValidatableResponse) -> Unit = { resp ->
    if (resp is ValidatableResponseImpl) {
        fn(resp.responseSpec)
    }
}