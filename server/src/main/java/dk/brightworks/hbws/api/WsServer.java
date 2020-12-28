package dk.brightworks.hbws.api;

import dk.brightworks.autowirer.wire.Service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface WsServer {
    String value() default "";
}
