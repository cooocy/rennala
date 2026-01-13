package er.rennala.autoconfig;

import er.rennala.request.filters.ct.CheckTokenFilter;
import er.rennala.request.filters.ct.CheckTokenProperties;
import er.rennala.request.filters.ctx.ContextFilter;
import er.rennala.request.filters.ctx.ContextProperties;
import er.rennala.request.filters.essential.EssentialFilter;
import er.rennala.request.filters.log.RequestLogFilter;
import er.rennala.request.filters.log.RequestLogProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
        EssentialFilter.class,
        RequestLogFilter.class,
        RequestLogProperties.class,
        ContextFilter.class,
        ContextProperties.class,
        CheckTokenFilter.class,
        CheckTokenProperties.class})
@AutoConfiguration
public class FiltersAutoConfiguration {

}
