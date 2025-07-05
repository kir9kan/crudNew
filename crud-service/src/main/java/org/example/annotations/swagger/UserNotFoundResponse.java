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
        responseCode = "404",
        description = "User not found",
        content = @Content(examples = @ExampleObject(
                value = """
                    {
                      "message": "User with Id 666 not found"
                    }
                    """
        )
        )
)
public @interface UserNotFoundResponse {}