package tw.idv.brandy.arrow.rest

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import tw.idv.brandy.arrow.KaqAppError
import tw.idv.brandy.arrow.repo.FruitRepo
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType

@WebServlet(name = "Hello", value = ["/fruits"])
class UndertowServlet : HttpServlet() {

    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        res.outputStream.print("in GET")
        res.contentType= MediaType.TEXT_PLAIN

    }
}