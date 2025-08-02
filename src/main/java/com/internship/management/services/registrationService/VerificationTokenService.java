package com.internship.management.services.registrationService;


import com.internship.management.entities.*;
import com.internship.management.mappers.RegistrationMapper;
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

    public String generateCode() {
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    public void createAndSendToken(Users user) {

        String code = generateCode();
        VerificationToken token = registrationMapper.verificationTokenUpdate(code, user);

        tokenRepository.save(token);
        sendEmail(user.getEmail(), code);
    }

    public void resendToken(Users user) {
        tokenRepository.deleteByUser(user);
        createAndSendToken(user);
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

    public void sendMsgToUser(String toEmail, Users user){

        SimpleMailMessage message = new SimpleMailMessage();

        String subject = "";
        String content = "";

        if(user instanceof Enterprise){

            subject = "Offer validation";
            content = "Dear " + user.getName() + "," + "\n" +
            "\n" + "We would like to inform you that your internship offer has been reviewed.\n";

        }
        if(user instanceof Student) {

            subject = "New Offer Approved";
            content = "Dear " + user.getName() + "," + "\n" +
                    "\n" + "We would like to inform you that one internship offer has been " +
                    "approved by a teacher of your department.\n";
        }
        if(user instanceof Teacher) {

            subject = "New Offer Arrival";
            content = "Dear " + user.getName() + "," + "\n" +
                    "\n" + "We would like to inform you there is a new offer to review regarding your department.\n";
        }

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content + " \n\nBest regards, Internship Platform.");

        mailSender.send(message);
    }
}
