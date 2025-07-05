package org.example.annotations.swagger;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponse(
        responseCode = "400",
        description = "User validation error",
        content = @Content(examples = @ExampleObject(
                value = """
                    {
                        "email": "Invalid Email",
                        "age": "Age must be at least 1",
                        "name": "must not be null"
                    }
                    """
        )
        )
)
public @interface UserBadRequest {}