package io.github.dineshsolanki.multitanent.mongo.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * Custom annotation to enable MongoDB repositories with a pre-configured mongoTemplateRef.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableMongoRepositories(
        mongoTemplateRef = "mongoTemplateShared"  // Default mongoTemplateRef
)
public @interface EnableSharedMongoRepositories {

    /**
     * Alias to forward the basePackages parameter to @EnableMongoRepositories.
     */
    @AliasFor(annotation = EnableMongoRepositories.class, attribute = "basePackages")
    String[] basePackages() default {};  // Allows user to specify basePackages

    /**
     * Allows user to specify a custom mongoTemplateRef (default is "mongoTemplateShared").
     */
    @AliasFor(annotation = EnableMongoRepositories.class, attribute = "mongoTemplateRef")
    String mongoTemplateRef() default "mongoTemplateShared";  // Default mongoTemplateRef
}

