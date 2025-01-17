package org.noear.nami.integration.springboot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import(AutoConfiguration.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableNamiClients {
}
