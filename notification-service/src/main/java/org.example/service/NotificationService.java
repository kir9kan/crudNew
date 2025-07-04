package org.example.service;

import lombok.extern.slf4j.Slf4j;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.dto.EmailDto;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    private final JavaMailSender emailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.emailSender = mailSender;
    }
    @KafkaListener(topics = "user-events")
    public void userEventHandler(ConsumerRecord<String, String> record){
        String message = "";
        String subject = "Уведомление пользователя";
        switch (record.key()){
            case "user-created" -> message = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
            case "user-deleted" -> message = "Здравствуйте! Ваш аккаунт был удалён.";
        }
        if (!message.isEmpty()) {
            sendEmail(new EmailDto(record.value(), message, subject));
            log.info("user-event message was sent");
        }
    }

    public void sendEmail(EmailDto emailDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.getEmail());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getMessage());
        emailSender.send(message);
    }
}