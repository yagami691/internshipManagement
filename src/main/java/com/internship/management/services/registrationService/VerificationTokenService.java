package com.internship.management.services.registrationService;


import com.internship.management.RegistrationMapper;
import com.internship.management.entities.Users;
import com.internship.management.entities.VerificationToken;
import com.internship.management.repositories.UsersRepository;
import com.internship.management.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final UsersRepository userRepository;
    private final RegistrationMapper registrationMapper;

    private final JavaMailSender mailSender;

    private final int EXPIRATION_MINUTES = 10;

    public String generateCode() {
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    public void createAndSendToken(Users user) {

        String code = generateCode();
        VerificationToken token = registrationMapper.verificationTokenUpdate(code, user);

        tokenRepository.save(token);
        sendEmail(user.getEmail(), code);
    }


    public Users verifyCode(String email, String inputCode) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        VerificationToken token = tokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("code not found with email: " + email));

        if (token.isUsed()) {
            throw new RuntimeException("This code has already been used.");
        }

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            String newCode = generateCode();
            VerificationToken newToken = registrationMapper.updateToken(token, newCode);
            tokenRepository.save(newToken);

            sendEmail(email, newCode);
            throw new RuntimeException("The code has expired. A new code has been sent to you.");
        }

        if (!token.getCode().equals(inputCode)) {
            throw new RuntimeException("incorrect code.");
        }

        token.setUsed(true);
        tokenRepository.save(token);

        user.setEmailVerified(true);
        return userRepository.save(user);
    }

    private void sendEmail(String toEmail, String code) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your account verification code");
        message.setText("Hello, \n\nHere is your verification code: " + code + " \n\nBest regards, Internship Platform.");

        mailSender.send(message);
    }
}
