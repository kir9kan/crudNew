package org.example.annotations.swagger;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponse(
        responseCode = "204",
        description = "User was deleted",
        headers = @Header(
                name = "Link to users",
                description = """
                        http://localhost:8080/users; type = "GET"
                        """,
                example = "http://localhost:8080/users"

        )
)
public @interface NoContentDeleteResponse {}