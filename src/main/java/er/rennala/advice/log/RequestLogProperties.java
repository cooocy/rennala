package er.rennala.advice.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "rennala.request-log")
public class RequestLogProperties {

    /**
     * 是否打印日志, 默认 true
     */
    private boolean enable = true;

    /**
     * 是否打印更多日志, 默认 false
     */
    private boolean verbose = false;

}
