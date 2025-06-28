package er.rennala;

import er.rennala.advice.service.monitor.ExecutionMonitorProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({ExecutionMonitorProcessor.class})
@AutoConfiguration
public class MonitorConfiguration {

}
