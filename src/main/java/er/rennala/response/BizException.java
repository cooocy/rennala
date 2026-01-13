package er.rennala.response;

import jakarta.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <p> Biz Exception, can be transformed to R by {@link R#err(BizException)}.
 * <p> Throw this exception in application layer to indicate a business failure, then the {@link GlobalExceptionHandler} will catch it and transform it to a standard response {@link R}.
 */
@ToString
@EqualsAndHashCode(callSuper = false, of = "code")
public class BizException extends RuntimeException {

    @Getter
    public final int code;

    public final String message;

    /**
     * New BizException by code and message.
     *
     * @param cm [nonnull] CM
     */
    public BizException(@Nonnull CM cm) {
        this.code = cm.code();
        this.message = cm.message();
    }

    /**
     * New BizException by code and custom message.
     *
     * @param cm      [nonnull] CM
     * @param message [nonempty] custom message
     */
    public BizException(@Nonnull CM cm,
                        @Nonnull String message) {
        this.code = cm.code();
        this.message = message;
    }

    @Nonnull
    @Override
    public String getMessage() {
        return message;
    }

}
