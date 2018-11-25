package org.redex.backend;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.redex.backend.config.FileConfig;
import org.redex.backend.security.DataSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@SpringBootApplication
@EnableConfigurationProperties({
    FileConfig.class
})
@EntityScan(value = "org.redex.model", basePackageClasses = {
    BackendApplication.class,
    Jsr310JpaConverters.class
})
public class BackendApplication {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ApplicationUser { }

    @Component
    public static class CurrentUserBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            ReflectionUtils.doWithFields(bean.getClass(), field -> {
                ReflectionUtils.makeAccessible(field);
                if (field.getAnnotation(ApplicationUser.class) != null) {
                    final Object proxyInstance = Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                            new Class[] { DataSession.class }, (proxy, method, args) -> {
                                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                                if (authentication != null && authentication.isAuthenticated()) {
                                    final Object principal = authentication.getPrincipal();
                                    return method.invoke(principal, args);
                                }
                                throw new NullPointerException();
                            });
                    field.set(bean, proxyInstance);
                }
            });
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }


    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
