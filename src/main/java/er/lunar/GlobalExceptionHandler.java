package er.lunar;

import er.carian.response.AbstractBizException;
import er.carian.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractBizException.class)
    public Result<Void> handle(AbstractBizException exception) {
        log.warn("[] Handle BizException ⬇", exception);
        return Result.err(exception.code, exception.getMessage());
    }

    /**
     * 访问不存在的 uri, 先进入此 handle, 再进入 RequestLogAdvice.doFilterInternal().
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handle(NoHandlerFoundException exception) {
        return Result.err(10002, "api not found.");
    }

    @ExceptionHandler(Throwable.class)
    public Result<Void> handle(Throwable throwable) {
        log.error("[] Handle Throwable ⬇", throwable);
        return Result.err(10001, throwable.getMessage());
    }

}
