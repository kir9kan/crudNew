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
        responseCode = "200",
        description = "User's information was updated",
        content = @Content(
                examples = @ExampleObject(
                        value = """
                    {
                          "id": 2,
                          "name": "Jonny Doe",
                          "email": "Jonny@example.com",
                          "age": 21,
                          "createdAt": "2025-07-05T03:28:40.999079"
                          "_links": {
                                  "self": {
                                      "href": "http://localhost:8080/users/1",
                                      "type": "GET"
                                  },
                                  "delete": {
                                      "href": "http://localhost:8080/users/1",
                                      "type": "DELETE"
                                  },
                                  "users": {
                                      "href": "http://localhost:8080/users",
                                      "type": "GET"
                                  }
                         }
                    }
                    """
                )
        )
)
public @interface OkUpdateUserResponse {
}