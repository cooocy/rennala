package er.rennala.advice.ctx;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * 用于读取 Request 中封装好的 Context.
 */
public class ContextReader {

    /**
     * 从 Request 中读取提前封装的 Context.
     */
    public static Context getContext(HttpServletRequest request) {
        return (Context) request.getAttribute(ContextKey.sCtx);
    }

    /**
     * 从 Request 中读取提前封装的 Profile.
     */
    public static Optional<AbstractProfile> getProfile(HttpServletRequest request) {
        Context ctx = getContext(request);
        return Optional.ofNullable(ctx.getProfile());
    }

}
