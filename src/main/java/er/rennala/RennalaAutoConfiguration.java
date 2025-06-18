package er.rennala;

import er.rennala.health.HealthController;
import er.rennala.health.HealthProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({HealthController.class, HealthProperties.class, GlobalExceptionHandler.class})
@AutoConfiguration
public class RennalaAutoConfiguration {

}
