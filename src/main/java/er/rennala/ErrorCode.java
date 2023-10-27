package er.rennala;

public interface ErrorCode {

    int SERVER_ERROR = 10001;

    int API_NOT_FOUND = 10002;

    int METHOD_NOT_SUPPORT = 10003;

    String METHOD_NOT_SUPPORT_S = "Method Not Support.";

    String API_NOT_FOUND_S = "API Not Found.";

    int TOKEN_NOT_FOUND_OR_VALID = 10009;

    String TOKEN_NOT_FOUND_OR_VALID_S = "Token Not Found or Invalid";

    int PARAMETER_ERROR = 10010;

}
