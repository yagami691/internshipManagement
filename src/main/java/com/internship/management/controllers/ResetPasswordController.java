package com.internship.management.controllers;

import com.internship.management.dto.ResetPasswordRequestDto;
import com.internship.management.entities.Users;
import com.internship.management.interfaces.PostOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path ="/resetPassword")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final PostOffer postOffer;
    private final PasswordEncoder passwordEncoder;

    @PatchMapping
    public void resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {

          Users user = postOffer.getUserByEmail(resetPasswordRequestDto.getEmail());
          user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getPassword()));

          postOffer.saveUser(user);
    }
}
