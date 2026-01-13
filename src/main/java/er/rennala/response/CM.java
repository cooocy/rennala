package er.rennala.response;

/**
 * <p> Code and Message interface.
 * <p> Used by {@link BizException} and {@link R}.
 */
public interface CM {

    int code();

    String message();

}
