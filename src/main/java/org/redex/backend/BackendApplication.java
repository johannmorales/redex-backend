package org.redex.backend;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.redex.backend.config.FileConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

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

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
