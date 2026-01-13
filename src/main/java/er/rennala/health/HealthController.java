package er.rennala.health;

import cn.hutool.core.util.StrUtil;
import er.rennala.response.R;
import er.rennala.z.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping
public class HealthController {

    private final AppProperties appProperties;

    private final HealthProperties healthProperties;

    @Autowired
    public HealthController(AppProperties appProperties,
                            HealthProperties healthProperties) {
        this.appProperties = appProperties;
        this.healthProperties = healthProperties;
    }

    @GetMapping("/")
    public R<Map<String, Object>> index() {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        m.put("app", appProperties.getAppName());
        m.put("ts", Instant.now());
        healthProperties.getEnv().forEach(k -> {
            String v = System.getenv(k);
            if (StrUtil.isEmpty(v)) {
                v = "";
            }
            m.put(k, v);
        });
        return R.ok(m);
    }

}
