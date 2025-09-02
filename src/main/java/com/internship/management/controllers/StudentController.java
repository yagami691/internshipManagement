package com.internship.management.controllers;

import com.internship.management.dto.StudentResponseDto;
import com.internship.management.dto.application.ApplicationRequestDto;
import com.internship.management.dto.application.ApplicationResponseDto;
import com.internship.management.dto.postOffer.OfferResponseDto;
import com.internship.management.dto.profile.*;
import com.internship.management.entities.*;
import com.internship.management.enums.ApplicationState;
import com.internship.management.enums.ConventionState;
import com.internship.management.enums.OfferStatus;
import com.internship.management.interfaces.NotificationInterface;
import com.internship.management.interfaces.PostOffer;
import com.internship.management.mappers.PostOfferMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "api/student")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "JWT")
public class StudentController {

    private final PostOffer postOffer;
    private final PostOfferMapper postOfferMapper;
    private final NotificationInterface notificationInterface;

    @GetMapping("/offersByApprovedStatus")
    public List<OfferResponseDto> getOfferByStatus(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<Offer> offers = postOffer.getOffersByStatusAndConventionApproved(OfferStatus.APPROVED, ConventionState.APPROVED, student.getDepartment());
        log.info("value {} ", student.isOnInternship());
        log.info("value {} ", student.getName());

        return student.isOnInternship() ? List.of() : postOfferMapper.toDtoList(offers);
    }

    @GetMapping("pendingApplicationsOfStudent")
    public List<ApplicationResponseDto> getPendingApplicationsOfStudent(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<Application> applications = postOffer.getApplicationsRejectedOrPendingByStudentEmail(email);

        List<Application> pendingOnly = applications.stream()
            .filter(app -> app.getState() == ApplicationState.PENDING)
            .toList();

        return student.isOnInternship() ? List.of() :  postOfferMapper.toDtoApplicationList(pendingOnly);
    }

    @GetMapping("/applicationsApprovedOfStudent")
    public List<ApplicationResponseDto> getApplicationsApprovedOfStudent(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        List<Application> applications  = postOffer.getApplicationsApprovedByStudentEmail(email);

        List<Application> approvedOnly = applications.stream()
            .filter(app -> app.getState() == ApplicationState.APPROVED)
            .toList();

        Application chosenApplicationByStudent = postOffer.getApplicationByStudentOnInternshipTrue(student);

        if(student.isOnInternship()){

            for(Application application : approvedOnly){

                if(!Objects.equals(application.getId(), chosenApplicationByStudent.getId())){
                    application.setState(ApplicationState.CANCELLED);
                }
            }

            return List.of();
        }

        return postOfferMapper.toDtoApplicationList(approvedOnly);
    }

    @DeleteMapping("/{application_id}")
    public void deleteApplication(@PathVariable("application_id") Long application_id){
        postOffer.deleteApplicationRejected(application_id);
    }

    @GetMapping("/filter")
    public List<OfferResponseDto> filter(@RequestParam Boolean paying,
                                         @RequestParam Boolean remote) {

        if(paying != null && remote != null) return postOfferMapper.toDtoList(postOffer.getOfferByPayingAndRemote(paying, remote));
        if(paying != null) return postOfferMapper.toDtoList(postOffer.getOfferPaying(paying));
        if(remote != null)  postOfferMapper.toDtoList(postOffer.getOfferRemote(remote));

        return List.of();
    }

    @PostMapping("{offer_id}/createApplication")
    public ResponseEntity<?> create(@ModelAttribute ApplicationRequestDto applicationRequestDto, @PathVariable Long offer_id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        if (student.isOnInternship()) {
            return ResponseEntity.badRequest().body("you are on internship and cannot apply.");
        }
        

        List<Application> allApplications = student.getApplications();
        boolean alreadyApplied = allApplications.stream()
            .anyMatch(app -> app.getOffer().getId().equals(offer_id) && 
                           (app.getState() == ApplicationState.PENDING || app.getState() == ApplicationState.APPROVED));
        
        if (alreadyApplied) {
            return ResponseEntity.badRequest().body("you already applied for this offer");
        }

        Application application = postOfferMapper.toEntity(applicationRequestDto);
        application.setStudent(student);

        Offer offer = postOffer.getOfferById(offer_id);
        Enterprise enterprise = offer.getEnterprise();
        application.setEnterprise(enterprise);
        application.setOffer(offer);

        postOffer.saveApplication(application);

        String enterpriseMsg = "New application received for the offer: " + offer.getTitle();
        notificationInterface.sendNotification(enterprise, enterpriseMsg);

        return ResponseEntity.ok(postOfferMapper.toDto(application));
    }

    @PutMapping("{application_id}/updateStudentStatus")
    public ResponseEntity<ApplicationResponseDto> updateStudentStatus(@PathVariable Long application_id, @RequestParam boolean applicationAccepted){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);

        Application application = postOffer.getApplicationApprovedById(application_id);

        if(applicationAccepted) {

            student.setOnInternship(true);
            application.setStudent(student);
            postOffer.saveUser(student);
            postOffer.saveApplication(application);
        }

        return ResponseEntity.ok(postOfferMapper.toDto(application));
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

        student.setLinkedinLink(linkedinRequestDto.getLinkedin());
        postOffer.saveUser(student);

        return  ResponseEntity.ok().body("linkedin link updated successfully");
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> getStudentStatus() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);
        
        if (student.isOnInternship()) {
            return ResponseEntity.ok().body(Map.of(
                "inInternship", true,
                "message", "Vous êtes en stage",
                "canApply", false
            ));
        }
        
        return ResponseEntity.ok().body(Map.of(
            "inInternship", false,
            "message", "Vous pouvez candidater aux offres",
            "canApply", true
        ));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<StudentResponseDto> getStudentProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = postOffer.getStudentByEmail(email);
        
        StudentResponseDto profileDto = postOfferMapper.toDtoStudent(student);
        return ResponseEntity.ok(profileDto);
    }
}
