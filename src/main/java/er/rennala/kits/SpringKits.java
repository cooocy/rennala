package er.rennala.kits;

import er.rennala.z.SpringApplicationException;
import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@AutoConfiguration
public class SpringKits implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ac) {
        ctx = ac;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory = beanFactory;
    }

    /**
     * <p> Get Bean by Bean Name.
     *
     * @param name Bean Name.
     * @param <T>  Generics Type
     * @return Bean Instance.
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException When not find.
     */
    @Nonnull
    public static <T> T getBean(@Nonnull String name) {
        return (T) getBeanFactory().getBean(name);
    }

    /**
     * <p> Get Bean by Class.
     *
     * @param clazz Bean Class.
     * @param <T>   Generics Type
     * @return Bean Instance.
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException When not find.
     */
    @Nonnull
    public static <T> T getBean(@Nonnull Class<T> clazz) {
        return getBeanFactory().getBean(clazz);
    }

    private static ListableBeanFactory getBeanFactory() {
        final ListableBeanFactory factory = null == beanFactory ? ctx : beanFactory;
        if (null == factory) {
            throw new SpringApplicationException("No ConfigurableListableBeanFactory or ApplicationContext injected, maybe not in the Spring environment?");
        }
        return factory;
    }

}
