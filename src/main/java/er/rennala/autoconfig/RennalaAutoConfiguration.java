package er.rennala.autoconfig;

import er.rennala.health.HealthController;
import er.rennala.health.HealthProperties;
import er.rennala.kits.SpringKits;
import er.rennala.response.GlobalExceptionHandler;
import er.rennala.z.AppProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({AppProperties.class, HealthController.class, HealthProperties.class, GlobalExceptionHandler.class, SpringKits.class})
@AutoConfiguration
public class RennalaAutoConfiguration {

}
