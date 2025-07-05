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
        description = "All users were found",
        content = @Content(
                examples = @ExampleObject(
                        value = """
                    {
                         "_embedded": {
                             "userDtoList": [
                                 {
                                     "id": 4,
                                     "name": "Jack Doe",
                                     "email": "Jack@example.ru",
                                     "age": 21,
                                     "createdAt": "2025-07-05T03:31:57.108628",
                                     "_links": {
                                         "self": {
                                             "href": "http://localhost:8080/users/4",
                                             "type": "GET"
                                         },
                                         "update": {
                                             "href": "http://localhost:8080/users",
                                             "type": "PUT"
                                         },
                                         "delete": {
                                             "href": "http://localhost:8080/users/4",
                                             "type": "DELETE"
                                         }
                                     }
                                 },
                                 {
                                     "id": 5,
                                     "name": "Jeff Don",
                                     "email": "Don@example.ru",
                                     "age": 35,
                                     "createdAt": "2025-07-05T03:33:47.738515",
                                     "_links": {
                                         "self": {
                                             "href": "http://localhost:8080/users/5",
                                             "type": "GET"
                                         },
                                         "update": {
                                             "href": "http://localhost:8080/users",
                                             "type": "PUT"
                                         },
                                         "delete": {
                                             "href": "http://localhost:8080/users/5",
                                             "type": "DELETE"
                                         }
                                     }
                                 }
                             ]
                         },
                         "_links": {
                             "self": {
                                 "href": "http://localhost:8080/users",
                                 "type": "GET"
                             },
                             "create": {
                                 "href": "http://localhost:8080/users",
                                 "type": "POST"
                             }
                         }
                     }
                    """
                )
        )
)
public @interface OkFindUsersResponse {}