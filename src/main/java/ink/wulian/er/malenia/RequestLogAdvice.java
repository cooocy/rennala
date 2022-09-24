package ink.wulian.er.malenia;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Order(10)
public class RequestLogAdvice extends OncePerRequestFilter {

    /**
     * 是否记录 Request 和 Response 详情
     */
    private final boolean LOG_VERBOSE;

    public RequestLogAdvice(boolean logVerbose) {
        this.LOG_VERBOSE = logVerbose;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long occurredAt = System.currentTimeMillis();
        String requestId = occurredAt + "-" + IdUtil.fastSimpleUUID().toUpperCase();

        request.setAttribute(RequestContextKey.OCCURRED_AT, occurredAt);
        request.setAttribute(RequestContextKey.REQUEST_ID, requestId);

        if (LOG_VERBOSE) {
            ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(req, res);

            String queryParams = URLDecoder.decode(req.getQueryString(), Charset.defaultCharset());
            if (queryParams == null) {
                queryParams = "";
            }
            Map<String, Object> verbose = new LinkedHashMap<>();
            verbose.put("id", requestId);
            verbose.put("uri", req.getRequestURI());
            verbose.put("queryParams", queryParams);

            // fixme: 当 otherFilter.doFilterInternal() 里为执行 filterChain.doFilter(), 这里的 req.getContentAsByteArray() 会获取到空.
            verbose.put("reqBody", new String(req.getContentAsByteArray()));
            verbose.put("resBody", new String(res.getContentAsByteArray()));

            log.info("[] Request Verbose -> {}", verbose);

            res.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }

        long end = System.currentTimeMillis();

        Map<String, Object> accessLogs = new LinkedHashMap<>();
        accessLogs.put(RequestContextKey.OCCURRED_AT, occurredAt);
        accessLogs.put(RequestContextKey.REQUEST_ID, requestId);
        accessLogs.put("uri", request.getRequestURI());
        accessLogs.put("duration", end - occurredAt + "ms");
        accessLogs.put("clientIp", getIp(request));
        accessLogs.put("userAgent", request.getHeader("user-agent"));

        log.info("[] Access -> {}", accessLogs);
    }

    private static String getIp(HttpServletRequest request) {
        Enumeration<String> xff = request.getHeaders("X-Forwarded-For");
        if (!xff.hasMoreElements()) {
            return "";
        }

        String xffHeader = xff.nextElement();
        return xffHeader.contains(",") ? xffHeader.substring(0, xffHeader.indexOf(",")) : xffHeader;
    }

}