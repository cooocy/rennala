package er.rennala.advice.log;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.IdUtil;

import er.rennala.advice.AdviceOrder;
import er.rennala.advice.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 记录 Request Access Log.
 * 封装 {@link Constants#sOccurredAt} 和 {@link Constants#sRequestId} 属性到 Request 中.
 * 记录 {@link Constants#sRequestId} 到 MDC.
 */
@Slf4j
@Order(AdviceOrder.log)
public class RequestLogAdvice extends OncePerRequestFilter {

    /**
     * 是否记录 Request 和 Response 详情
     */
    private final boolean LOG_VERBOSE;

    public RequestLogAdvice() {
        this.LOG_VERBOSE = false;
    }

    public RequestLogAdvice(boolean logVerbose) {
        this.LOG_VERBOSE = logVerbose;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Instant occurredAt = Instant.now();
        long occurredAtMs = occurredAt.toEpochMilli();
        String requestId = occurredAtMs + "-" + IdUtil.fastSimpleUUID().toUpperCase();

        request.setAttribute(Constants.sOccurredAt, occurredAt);
        request.setAttribute(Constants.sRequestId, requestId);

        MDC.put(Constants.sRequestId, requestId);

        response.addHeader("X-Request-Id", requestId);

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

            // fixme: 当 otherFilter.doFilterInternal() 里未执行 filterChain.doFilter(), 这里的 req.getContentAsByteArray() 会获取到空.
            verbose.put("reqBody", new String(req.getContentAsByteArray()));
            verbose.put("resBody", new String(res.getContentAsByteArray()));

            log.info("[] Request Verbose -> {}", verbose);

            res.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }

        long end = System.currentTimeMillis();

        Map<String, Object> accessLogs = new LinkedHashMap<>();
        accessLogs.put(Constants.sOccurredAt, occurredAt);
        accessLogs.put(Constants.sRequestId, requestId);
        accessLogs.put("uri", request.getRequestURI());
        accessLogs.put("duration", end - occurredAtMs + "ms");
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