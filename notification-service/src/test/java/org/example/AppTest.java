package org.example;

import org.example.dto.EmailDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka
@ActiveProfiles("test")
class AppTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private GreenMail greenMail;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void setup() {
        greenMail = new GreenMail(new ServerSetup(3025, null, "smtp"));
        greenMail.start();
    }
    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void sending_user_created_event_and_email_is_successful() throws Exception {
        kafkaTemplate.send("user-events", "user-created", "test@test.ru");
        greenMail.waitForIncomingEmail(10000, 1);
        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertTrue(messages.length > 0);
        assertTrue(messages[0].getContent().toString().contains("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."));
    }

    @Test
    void sending_user_deleted_event_and_email_is_successful() throws Exception {
        kafkaTemplate.send("user-events", "user-deleted", "test@test.ru");
        greenMail.waitForIncomingEmail(10000, 1);
        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertTrue(messages.length > 0);
        assertTrue(messages[0].getContent().toString().contains("Здравствуйте! Ваш аккаунт был удалён."));
    }

    @Test
    void sendEmail_controller_request_with_valid_data_is_successful() throws Exception {
        EmailDto emailDto = new EmailDto("test@test.ru", "testM", "testS");

        ResponseEntity<String> response = testRestTemplate.postForEntity( "/email", emailDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Email was sent to " + emailDto.getEmail());

        greenMail.waitForIncomingEmail(10000, 1);
        MimeMessage[] messages = greenMail.getReceivedMessages();

        assertEquals(1, messages.length);
        assertThat(messages[0].getAllRecipients()[0].toString()).isEqualTo(emailDto.getEmail());
        assertThat(messages[0].getSubject()).isEqualTo(emailDto.getSubject());
        assertThat(messages[0].getContent().toString()).contains(emailDto.getMessage());
    }
    @Test
    void sendEmail_controller_request_with_empty_data_is_prohibited() {
        EmailDto emailDto = new EmailDto("", "", "");

        ResponseEntity<String> response = testRestTemplate.postForEntity( "/email", emailDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("Email is required")
                .contains("Message is required");
        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }
    @Test
    void sendEmail_controller_request_with_null_data_is_prohibited() {
        EmailDto emailDto = new EmailDto(null, null, null);

        ResponseEntity<String> response = testRestTemplate.postForEntity( "/email", emailDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("Email is required")
                .contains("Message is required");
        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }
    @Test
    void sendEmail_controller_request_with_invalid_email_is_prohibited() {
        EmailDto emailDto = new EmailDto("invalidEmail", "testM", "testS");

        ResponseEntity<String> response = testRestTemplate.postForEntity( "/email", emailDto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid Email");
        assertThat(greenMail.getReceivedMessages()).isEmpty();
    }
}