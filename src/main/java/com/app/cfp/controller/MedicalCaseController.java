package com.app.cfp.controller;

import com.app.cfp.dto.MedicalCaseCustomizedDTO;
import com.app.cfp.dto.MedicalCaseFullDTO;
import com.app.cfp.dto.StringResponseDTO;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.mapper.MedicalCasesCustomizedMapper;
import com.app.cfp.mapper.MedicalCasesMapper;
import com.app.cfp.service.AllocationService;
import com.app.cfp.service.MedicalCaseService;
import com.app.cfp.utils.ImageUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cases")
@AllArgsConstructor
@CrossOrigin
public class MedicalCaseController {

    private final MedicalCaseService medicalCasesService;

    private final MedicalCasesMapper medicalCasesMapper;

    private final MedicalCasesCustomizedMapper medicalCasesCustomizedMapper;

    private final AllocationService allocationService;


    @GetMapping("/assigned/incomplete")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllAssignedIncompleteMedicalCases(Principal principal, Pageable pageable, @Param("encodedInfo") String encodedInfo) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllIncompleteCasesForResident(principal.getName(), pageable, encodedInfo);

        return new ResponseEntity<>(medicalCasesPage.map(medicalCasesMapper::toDto), HttpStatus.OK);
    }

    @GetMapping("/assigned/all")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<List<MedicalCaseFullDTO>> getAllAssignedMedicalCases(Principal principal) {
        List<MedicalCase> medicalCases = medicalCasesService.getAllCasesForResident(principal.getName());
        return new ResponseEntity<>(medicalCases.stream().map(medicalCasesMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/assigned/completed")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllAssignedCompleteMedicalCases(Principal principal, Pageable pageable, @Param("diagnostic") String diagnostic) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllCompletedCasesForResident(principal.getName(), pageable, diagnostic);
        return new ResponseEntity<>(medicalCasesPage.map(medicalCasesMapper::toDto), HttpStatus.OK);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllCompletedMedicalCases(Pageable pageable, @Param("encodedInfo") String encodedInfo) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllCompletedByResidentCases(pageable, encodedInfo);
        return new ResponseEntity<>(medicalCasesPage.map(medicalCasesMapper::toDto), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<Set<MedicalCaseCustomizedDTO>> getAllMedicalCasesAssignedTo(@RequestParam(value = "encodedInfo") String encodedInfo) {
        Set<MedicalCase> medicalCases = medicalCasesService.getAllMedicalCasesAssignedTo(encodedInfo);
        return new ResponseEntity<>(medicalCases.stream().map(medicalCasesCustomizedMapper::toDto).collect(Collectors.toSet()), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<MedicalCaseFullDTO> addMedicalCase(@RequestPart("image") MultipartFile image, @RequestPart("medicalCase") String medicalCaseJSON) throws IOException {
        System.out.println(image);
        ObjectMapper objectMapper = new ObjectMapper();
        MedicalCase medicalCase = objectMapper.readValue(medicalCaseJSON, MedicalCase.class);
        medicalCase.setCFPImage(ImageUtility.compressImage(image.getBytes()));

        MedicalCase returnedMedicalCase = medicalCasesService.addMedicalCase(medicalCase);

        return new ResponseEntity<>(medicalCasesMapper.toDto(returnedMedicalCase), HttpStatus.CREATED);
    }

    @PostMapping("/drawing")
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<StringResponseDTO> addDrawing(@RequestPart("image") MultipartFile image, @RequestPart("id") String medicalCaseJSON) throws IOException {
        MedicalCase returnedMedicalCase = medicalCasesService.addDrawingToMedicalCase(UUID.fromString(medicalCaseJSON), ImageUtility.compressImage(image.getBytes()));

        return new ResponseEntity<>(StringResponseDTO.builder().message("Cazul medical cu id-ul " + returnedMedicalCase.getId() + " a fost actualizat!").build(), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<MedicalCaseFullDTO> updateMedicalCase(@RequestBody MedicalCaseFullDTO medicalCaseDTO) {
        MedicalCase returnedMedicalCase = medicalCasesService.updateMedicalCase(medicalCasesMapper.toDomain(medicalCaseDTO));
        return new ResponseEntity<>(medicalCasesMapper.toDto(returnedMedicalCase), HttpStatus.CREATED);
    }

//    @GetMapping("/assignEducationalCase")
//    @PreAuthorize("hasAnyRole('ROLE_OPERATOR')")
//    public ResponseEntity<StringResponseDTO> assignVirtualCase() {
//        allocationService.assignEducationalCases();
//        return new ResponseEntity<>(StringResponseDTO.builder().message("Successfully assign education case").build(), HttpStatus.OK);
//    }
}
