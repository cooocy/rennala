package ink.wulian.er.malenia;

import ink.wulian.er.godrick.response.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RequestMapping
@RestController
public class HealthController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public Result<Map<String, Object>> index() {
        Map<String, Object> m = Map.of("app", appName, "serverTime", Instant.now());
        return Result.ok(m);
    }

}
