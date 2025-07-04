package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDto {
    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;
    private String subject;

    @NotBlank(message = "Message is required")
    private String message;

    public EmailDto() {}

    public EmailDto(String email, String message, String subject) {
        this.email = email;
        this.message = message;
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
