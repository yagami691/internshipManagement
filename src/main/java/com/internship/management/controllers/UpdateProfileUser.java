package com.internship.management.controllers;


import com.internship.management.dto.profile.EmailRequestDto;
import com.internship.management.dto.profile.PasswordRequestDto;
import com.internship.management.entities.Users;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "updateProfile")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class UpdateProfileUser {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordRequestDto passwordRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = postOffer.getUserByEmail(email);

        user.setPassword(passwordRequestDto.getPassword());
        postOffer.saveUser(user);
        return ResponseEntity.ok().body("password updated successfully");
    }

    @PatchMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestBody EmailRequestDto emailRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = postOffer.getUserByEmail(email);

        user.setEmail(emailRequestDto.getEmail());
        postOffer.saveUser(user);

        return ResponseEntity.ok().body(user.getName() + " email updated successfully");
    }

    @DeleteMapping("/deleteTeacherAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = postOffer.getUserByEmail(email);
        postOffer.deleteUser(user.getId());

        return ResponseEntity.ok( user.getName() + " deleted successfully");
    }
}
