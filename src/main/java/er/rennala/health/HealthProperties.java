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
@ConfigurationProperties(prefix = "rennala.health")
public class HealthProperties {

    private Set<String> env = new HashSet<>();

}