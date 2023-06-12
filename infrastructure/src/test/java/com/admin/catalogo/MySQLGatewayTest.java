package com.admin.catalogo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(includeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
})
@ExtendWith(CleanUpExtension.class)
public @interface MySQLGatewayTest {
}
