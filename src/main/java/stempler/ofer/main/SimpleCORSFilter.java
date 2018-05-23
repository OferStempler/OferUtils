package stempler.ofer.main;

/**
 * Created by ofer on 23/05/18.
 */
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// A web Filter example
// NOTE: A FILTER with "Access-Control-Allow" headers IS a MUST for CRM Cross-Origin !!!!!!!!!
//       Otherwise - ws connection from other origin will not succeed..
@Component
//@WebFilter(urlPatterns={"/dev987654321.filter.will.never.capture.this"})
//@WebFilter(urlPatterns={"/*"})
public class SimpleCORSFilter implements Filter {

    //                private final Logger logger = LoggerFactory.getLogger(SimpleCORSFilter.class);
    Logger logger = Logger.getLogger(this.getClass());
    public SimpleCORSFilter() {
        logger.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.trace("in Filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.trace("SimpleCORSFilter.doFilter() - " + "request.getRequestURI():[" + request.getRequestURI() + "]");
        logger.trace("SimpleCORSFilter.doFilter() - " + "request.getRequestURL():[" + request.getRequestURL() + "]");
        logger.trace("SimpleCORSFilter.doFilter() - Adding Access Control Response Headers");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.debug("=======================================================");
        logger.debug("SimpleCORSFilter.init() - starting CorsFilter Filter for cors");
        logger.debug("=======================================================");
    }

    @Override
    public void destroy() {
    }

}