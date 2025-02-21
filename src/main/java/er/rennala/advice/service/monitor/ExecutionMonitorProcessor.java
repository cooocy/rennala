package er.rennala.advice.service.monitor;

import cn.hutool.core.date.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
public class ExecutionMonitorProcessor {

    @Around("@annotation(er.rennala.advice.service.monitor.ExecutionMonitor) || @within(er.rennala.advice.service.monitor.ExecutionMonitor)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("[] {}.{} Begins. input: {}", className, methodName, objects2String(joinPoint.getArgs()));

        Object returned = joinPoint.proceed();
        sw.stop();
        log.info("[] {}.{} Ends. time: {}ms, output: {}", className, methodName, sw.getLastTaskTimeMillis(), object2String(returned));

        return returned;
    }

    private String objects2String(Object[] objects) {
        if (Objects.isNull(objects) || objects.length == 0) {
            return "[empty]";
        }
        if (objects.length == 1) {
            return object2String(objects[0]);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < objects.length; i++) {
            sb.append(object2String(objects[i]));
            if (i < objects.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String object2String(Object object) {
        if (Objects.isNull(object)) {
            return "(NULL)";
        }
        if (object instanceof Collection) {
            Collection c = (Collection) object;
            return "(" + c.getClass().getSimpleName() + ": size=" + c.size() + ")";
        }
        if (object instanceof Map) {
            Map m = (Map) object;
            return "(" + m.getClass().getSimpleName() + ": size=" + m.size() + ")";
        }
        return "(" + object + ")";
    }

}
