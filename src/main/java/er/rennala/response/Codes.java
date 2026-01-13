package er.rennala.response;

/**
 * <p> Standard Codes and Messages for API responses.
 * <p> Used by {@link BizException} and {@link R}.
 */
public enum Codes implements CM {

    Ok(0, "OK"),

    ApplicationStartingError(101, "Application Starting Error."),

    ServerError(102, "Server Error."),

    DataError(103, "Data Error."),

    ApiNotFound(120, "Api Not Found."),

    MethodNotSupport(121, "Method Not Support."),

    TokenInvalid(122, "Token Not Found or Invalid"),

    ProfileNotSupport(123, "Profile Not Support"),

    ArgsIllegal(124, "Args Illegal."),

    RecordNotFound(125, "Record not found."),

    ;


    private final int code;

    private final String message;

    Codes(int code,
          String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

}
