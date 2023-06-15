package er.rennala.advice.ctx;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * 用于读取 Request 中封装好的 Context.
 */
public class ContextReader {

    /**
     * 从 Request 中读取提前封装的 Context.
     */
    @Nonnull
    public static Context getContext(@Nonnull HttpServletRequest request) {
        return (Context) request.getAttribute(ContextKey.sCtx);
    }

    /**
     * 从 Request 中读取提前封装的 Profile.
     */
    @Nonnull
    public static Optional<AbstractProfile> getProfile(@Nonnull HttpServletRequest request) {
        Context ctx = (Context) request.getAttribute(ContextKey.sCtx);
        return Optional.ofNullable(ctx.getProfile());
    }

}
