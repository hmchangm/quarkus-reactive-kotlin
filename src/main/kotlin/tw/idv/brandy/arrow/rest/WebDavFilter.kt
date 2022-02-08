package tw.idv.brandy.arrow.rest

import org.jboss.logging.Logger
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.MediaType


@WebFilter(filterName = "WebDavFilter", urlPatterns = ["/*"], asyncSupported = true)
class WebDavFilter : Filter {
    private val logger: Logger = Logger.getLogger(WebDavFilter::class.java)

    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        when (request.method) {
            "LOCK" -> {
                logger.info(request.method)
                logger.info(request.inputStream.readBytes().let { String(it) })
                response.contentType = MediaType.TEXT_XML
                response.writer.print(
                    """<?xml version="1.0" encoding="UTF-8"?>
                <d:prop xmlns:d="DAV:"><d:lockdiscovery><d:activelock><d:locktype><d:write/></d:locktype>
                <d:lockscope><d:exclusive/></d:lockscope><d:depth>Infinity</d:depth><d:timeout>Second-604800</d:timeout>
                <d:locktoken><d:href>704b1f31-21c0-4af0-865e-637be0b524d7</d:href></d:locktoken>                
                <d:lockroot><d:href>/financial/costs.xlsx</d:href></d:lockroot></d:activelock></d:lockdiscovery></d:prop>"""
                )
            }
            "PROPFIND" -> {
                logger.info(request.method)
                logger.info(request.inputStream.readBytes().let { String(it) })
                response.contentType = MediaType.TEXT_XML
                response.writer.print("""<?xml version="1.0" encoding="UTF-8"?><d:multistatus xmlns:d="DAV:"><d:response><d:href>/financial/costs.xlsx</d:href><d:propstat><d:prop><d:creationdate>2022-01-18T15:01:49Z</d:creationdate><d:getlastmodified>Tue, 18 Jan 2022 15:01:49 GMT</d:getlastmodified><d:getcontentlength>29224</d:getcontentlength></d:prop><d:status>HTTP/1.1 200 Success</d:status></d:propstat><d:propstat><d:prop><modifiedby/></d:prop><d:status>HTTP/1.1 404 Not Found</d:status></d:propstat></d:response></d:multistatus>""")
            }
            else -> chain.doFilter(request, response)
        }
        println(request.method)

    }


}