package er.rennala.response;

import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * <p> Standard Response.
 * <p> Use {@link R#ok()} or {@link R#ok(Object)} to create a success response.
 * <p> Use {@link R#err(BizException)} to create a failure response.
 *
 * @param <T> type of data
 */
@ToString
public class R<T> {

    public final boolean success;

    public final int code;

    public final String message;

    @Nullable
    public final T data;

    public static R<Void> ok() {
        return new R<>(true, Codes.Ok.code(), Codes.Ok.message(), null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, Codes.Ok.code(), Codes.Ok.message(), data);
    }

    public static R<Void> err(BizException be) {
        return new R<>(false, be.code, be.message, null);
    }

    private R(boolean success,
              int code,
              String message,
              T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

}