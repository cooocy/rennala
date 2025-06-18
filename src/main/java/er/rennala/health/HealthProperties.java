package er.rennala.health;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "health")
public class HealthProperties {

    private System system;

    @ToString
    @Getter
    @Setter
    public static class System {

        private Set<String> properties = new HashSet<>();

    }

}
