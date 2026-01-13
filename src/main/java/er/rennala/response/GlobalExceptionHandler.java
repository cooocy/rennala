package er.rennala.response;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    /**
     * <p> 处理 validation 参数校验不通过的异常
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handle(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String messages = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return R.err(new BizException(Codes.ArgsIllegal, messages));
    }

    /**
     * <p> 处理请求体无法解析的异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handle(HttpMessageNotReadableException exception) {
        log.warn("[RNA-GEH] Handle BizException ⬇", exception);
        return R.err(new BizException(Codes.ArgsIllegal));
    }

    /**
     * <p> 访问不存在的 uri, 先进入此 handle, 再进入 RequestLogFilter.doFilterInternal().
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handle(NoHandlerFoundException exception) {
        return R.err(new BizException(Codes.ApiNotFound));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handle(HttpRequestMethodNotSupportedException exception) {
        return R.err(new BizException(Codes.MethodNotSupport));
    }

    /**
     * <p> 处理其他未知异常
     */
    @ExceptionHandler(Throwable.class)
    public R<Void> handle(Throwable throwable) {
        log.error("[RNA-GEH] Handle Throwable ⬇", throwable);
        return R.err(new BizException(Codes.ServerError, ExceptionUtil.getRootCauseMessage(throwable)));
    }

}
