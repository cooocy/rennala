package er.rennala;

import er.carian.response.AbstractBizException;
import er.carian.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static er.rennala.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractBizException.class)
    public R<Void> handle(AbstractBizException exception) {
        log.warn("[] Handle BizException ⬇", exception);
        return R.err(exception.code, exception.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handle(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String messages = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return R.err(PARAMETER_ERROR, messages);
    }

    /**
     * 访问不存在的 uri, 先进入此 handle, 再进入 RequestLogAdvice.doFilterInternal().
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handle(NoHandlerFoundException exception) {
        return R.err(API_NOT_FOUND, API_NOT_FOUND_S);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handle(HttpRequestMethodNotSupportedException exception) {
        return R.err(METHOD_NOT_SUPPORT, METHOD_NOT_SUPPORT_S);
    }

    @ExceptionHandler(Throwable.class)
    public R<Void> handle(Throwable throwable) {
        log.error("[] Handle Throwable ⬇", throwable);
        return R.err(SERVER_ERROR, throwable.getMessage());
    }

}
