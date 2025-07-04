package er.rennala;

import er.rennala.advice.ct.CheckTokenAdvice;
import er.rennala.advice.ct.CheckTokenProperties;
import er.rennala.advice.ctx.ContextAdvice;
import er.rennala.advice.ctx.ContextLogProperties;
import er.rennala.advice.log.RequestLogAdvice;
import er.rennala.advice.log.RequestLogProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({RequestLogAdvice.class, ContextAdvice.class, CheckTokenAdvice.class, RequestLogProperties.class, ContextLogProperties.class, CheckTokenProperties.class})
@AutoConfiguration
public class DefaultAdviceConfiguration {

}
