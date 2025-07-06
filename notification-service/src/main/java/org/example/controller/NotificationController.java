package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.EmailDto;
import org.example.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailDto emailDto){
        notificationService.sendEmail(emailDto);
        return ResponseEntity.status(HttpStatus.OK).body("Email was sent to " + emailDto.getEmail());
    }
}