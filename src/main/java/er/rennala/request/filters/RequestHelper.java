package er.rennala.request.filters;

import er.rennala.domain.FilterException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Enumeration;

/**
 * 用于在 Request 中存取一些属性.
 */
public class RequestHelper {

    private static final String sOccurredAt = "occurredAt";

    private static final String sRequestId = "requestId";

    /**
     * 将请求发生时间放入 Request 属性中.
     *
     * @param request    [nonnull] HttpServletRequest
     * @param occurredAt [nonnull] 请求发生时间
     */
    public static void putOccurredAt(HttpServletRequest request, Instant occurredAt) {
        request.setAttribute(sOccurredAt, occurredAt);
    }

    /**
     * 从 Request 属性中获取请求发生时间.
     *
     * @param request [nonnull] HttpServletRequest
     * @return [nonnull] 请求发生时间
     * @throws FilterException 如果请求中没有该属性
     */
    public static Instant getOccurredAt(HttpServletRequest request) {
        Object occurredAt = request.getAttribute(sOccurredAt);
        if (occurredAt instanceof Instant) {
            return (Instant) occurredAt;
        }
        throw new FilterException("[RNA-Filter] Can not find attr `occurredAt` in request.");
    }

    /**
     * 将请求 ID 放入 Request 属性中.
     *
     * @param request   [nonnull] HttpServletRequest
     * @param requestId [nonnull] 请求 ID
     */
    public static void putRequestId(HttpServletRequest request, String requestId) {
        request.setAttribute(sRequestId, requestId);
    }

    /**
     * 从 Request 属性中获取请求 ID.
     *
     * @param request [nonnull] HttpServletRequest
     * @return [nonnull] 请求 ID
     * @throws FilterException 如果请求中没有该属性
     */
    public static String getRequestId(HttpServletRequest request) {
        Object requestId = request.getAttribute(sRequestId);
        if (requestId instanceof String) {
            return (String) requestId;
        }
        throw new FilterException("[RNA-Filter] Can not find attr `requestId` in request.");
    }

    /**
     * 从 Request Header 中获取客户端 IP.
     *
     * @param request [nonnull] HttpServletRequest
     * @return [nonnull] 客户端 IP, 如果没有则返回空字符串
     */
    public static String getIp(HttpServletRequest request) {
        Enumeration<String> xff = request.getHeaders("X-Forwarded-For");
        if (!xff.hasMoreElements()) {
            return "";
        }

        String xffHeader = xff.nextElement();
        return xffHeader.contains(",") ? xffHeader.substring(0, xffHeader.indexOf(",")) : xffHeader;
    }

}
