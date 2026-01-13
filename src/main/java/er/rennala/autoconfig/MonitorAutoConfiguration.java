package er.rennala.autoconfig;

import er.rennala.advice.monitor.ExecutionMonitorProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({ExecutionMonitorProcessor.class})
@AutoConfiguration
public class MonitorAutoConfiguration {

}
