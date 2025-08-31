package er.rennala;

import cn.hutool.core.exceptions.ExceptionUtil;
import er.carian.response.BizException;
import er.carian.response.Codes;
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


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public R<Void> handle(BizException exception) {
        log.warn("[RNA-GEH] Handle BizException ⬇", exception);
        return R.err(exception);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handle(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String messages = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return R.err(new BizException(Codes.ArgsIllegal, messages));
    }

    /**
     * 访问不存在的 uri, 先进入此 handle, 再进入 RequestLogFilter.doFilterInternal().
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handle(NoHandlerFoundException exception) {
        return R.err(new BizException(Codes.ApiNotFound));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handle(HttpRequestMethodNotSupportedException exception) {
        return R.err(new BizException(Codes.MethodNotSupport));
    }

    @ExceptionHandler(Throwable.class)
    public R<Void> handle(Throwable throwable) {
        log.error("[RNA-GEH] Handle Throwable ⬇", throwable);
        return R.err(new BizException(Codes.ServerError, ExceptionUtil.getMessage(throwable)));
    }

}
