package com.internship.management.controllers;

import com.internship.management.dto.application.ApplicationRequestDto;
import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.application.NotificationDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.dto.profile.*;
import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @GetMapping("/offersByApprovedStatus")
    public List<OfferResponseDto> getOfferByStatus(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Student student = postOffer.getStudentByEmail(email);
        List<Offer> offers = postOffer.getOffersByStatusAndConventionApproved(OfferStatus.APPROVED, ConventionState.APPROVED, student.getDepartment());
        log.info("value {} ", student.isOnInternship());
        log.info("value {} ", student.getName());

        return student.isOnInternship() ? List.of() : postOfferMapper.toDtoList(offers);
    }

    @GetMapping("/filter")
    public List<OfferResponseDto> filter(@RequestParam Boolean paying,
                                         @RequestParam Boolean remote) {

        if(paying != null && remote != null) return postOfferMapper.toDtoList(postOffer.getOfferByPayingAndRemote(paying, remote));
        if(paying != null) return postOfferMapper.toDtoList(postOffer.getOfferPaying(paying));
        if(remote != null)  postOfferMapper.toDtoList(postOffer.getOfferRemote(remote));

        return List.of();
    }

    @GetMapping("/StudentNotifications")
    public ResponseEntity<List<NotificationDto>> getUnseenNotifications() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<Notification> unseen = notificationInterface.getAllUnSeenNotificationsByUser(student);

        return ResponseEntity.ok(
                unseen.stream()
                        .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.getCreatedAt()))
                        .toList()
        );
    }


    @PostMapping("{offer_id}/createApplication")
    public ApplicationResponseDto create(@ModelAttribute ApplicationRequestDto applicationRequestDto, @PathVariable Long offer_id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Application application = postOfferMapper.toEntity(applicationRequestDto);
        Student student = postOffer.getStudentByEmail(email);
        application.setStudent(student);

        Offer offer = postOffer.getOfferById(offer_id);
        Enterprise enterprise = offer.getEnterprise();
        application.setEnterprise(enterprise);
        application.setOffer(offer);

        postOffer.saveApplication(application);

        String enterpriseMsg = "New application received for the offer: " + offer.getTitle();
        notificationInterface.sendNotification(enterprise, enterpriseMsg);

        return postOfferMapper.toDto(application);
    }

     @PutMapping("updateStudentStatus")
     public ResponseEntity<String> updateStudentStatus(){

         String email = SecurityContextHolder.getContext().getAuthentication().getName();
         Student student = postOffer.getStudentByEmail(email);

        List<Application> applications = postOffer.getByApprovedOrRejectedApplication(student.getId());
        for(Application application : applications){
            if(application.getState() ==  ApplicationState.APPROVED){
                student.setOnInternship(true);
                postOffer.saveUser(student);
                return ResponseEntity.ok(student.getName() + " is on internship");
            };
        }

        return ResponseEntity.ok(student.getName() +
                " can still apply because all of his applications was rejected");
     }


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

        return ResponseEntity.ok().body(user.getName() + " student email updated successfully");
    }


    @PatchMapping("updateLanguages")
    public ResponseEntity<String> updateLanguages(@RequestBody LanguageRequestDto languageRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<String> languages = student.getLanguages();
        languages.add(languageRequestDto.getLanguage());
        student.setLanguages(languages);

        postOffer.saveUser(student);

        return ResponseEntity.ok().body("language updated successfully");
    }

    @PatchMapping("/updateGithubLink")
    public ResponseEntity<String> updateGithubLink(@RequestBody GithubRequestDto githubRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        student.setGithubLink(githubRequestDto.getGithub());
        postOffer.saveUser(student);

        return  ResponseEntity.ok().body("github link updated successfully");
    }

    @PatchMapping("/updateLinkedinLink")
    public ResponseEntity<String> updateLinkedinLink(@RequestBody LinkedinRequestDto linkedinRequestDto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        student.setGithubLink(linkedinRequestDto.getLinkedin());
        postOffer.saveUser(student);

        return  ResponseEntity.ok().body("github link updated successfully");
    }

    @DeleteMapping("/deleteStudentAccount")
    public ResponseEntity<String> delete(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student Student = postOffer.getStudentByEmail(email);
        postOffer.deleteUser(Student.getId());

        return ResponseEntity.ok("Student deleted successfully");

    }
}

