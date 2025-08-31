package er.rennala.request.filters.ct;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "rennala.filters.check-token")
public class CheckTokenProperties {

    /**
     * 是否开启检查 token, 默认 true
     */
    private boolean enable = true;

    /**
     * black/white
     */
    private String mode;

    /**
     * 匹配的 uri, 以 '/' 开头
     */
    private Set<String> uris = new HashSet<>();

}
