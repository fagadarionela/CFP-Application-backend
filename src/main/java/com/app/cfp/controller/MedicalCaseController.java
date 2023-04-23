package com.app.cfp.controller;

import com.app.cfp.dto.MedicalCaseCustomizedDTO;
import com.app.cfp.dto.MedicalCaseFullDTO;
import com.app.cfp.dto.StringResponseDTO;
import com.app.cfp.entity.MedicalCase;
import com.app.cfp.mapper.MedicalCasesCustomizedMapper;
import com.app.cfp.mapper.MedicalCasesMapper;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/cases")
@AllArgsConstructor
@CrossOrigin
public class MedicalCaseController {

    private final MedicalCaseService medicalCasesService;

    private final MedicalCasesMapper medicalCasesMapper;

    private final MedicalCasesCustomizedMapper medicalCasesCustomizedMapper;

    @GetMapping("/assigned/incomplete")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllAssignedIncompleteMedicalCases(Principal principal, Pageable pageable, @Param("encodedInfo") String encodedInfo) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllIncompleteCasesForResident(principal.getName(), pageable, encodedInfo);

        return new ResponseEntity<>(medicalCasesPage.map(medicalCase -> {
            MedicalCaseFullDTO medicalCaseDTO = medicalCasesMapper.toDto(medicalCase);
            medicalCaseDTO.setCFPImage(ImageUtility.decompressImage(medicalCaseDTO.getCFPImage()));
            if (medicalCaseDTO.getCFPImageCustomized() != null) {
                medicalCaseDTO.setCFPImageCustomized(ImageUtility.decompressImage(medicalCaseDTO.getCFPImageCustomized()));
            }
            return medicalCaseDTO;
        }), HttpStatus.OK);
    }

    @GetMapping("/assigned/completed")
    @PreAuthorize("hasRole('ROLE_RESIDENT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllAssignedCompleteMedicalCases(Principal principal, Pageable pageable, @Param("diagnostic") String diagnostic) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllCompletedCasesForResident(principal.getName(), pageable, diagnostic);

        return new ResponseEntity<>(medicalCasesPage.map(medicalCase -> {
            MedicalCaseFullDTO medicalCaseDTO = medicalCasesMapper.toDto(medicalCase);
            medicalCaseDTO.setCFPImage(ImageUtility.decompressImage(medicalCaseDTO.getCFPImage()));
            return medicalCaseDTO;
        }), HttpStatus.OK);
    }

    @GetMapping("/completed")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<Page<MedicalCaseFullDTO>> getAllCompletedMedicalCases(Pageable pageable, @Param("encodedInfo") String encodedInfo) {
        Page<MedicalCase> medicalCasesPage = medicalCasesService.getAllCompletedByResidentCases(pageable, encodedInfo);
        return new ResponseEntity<>(medicalCasesPage.map(medicalCase -> {
            MedicalCaseFullDTO medicalCaseDTO = medicalCasesMapper.toDto(medicalCase);
            medicalCaseDTO.setCFPImage(ImageUtility.decompressImage(medicalCaseDTO.getCFPImage()));
            if (medicalCaseDTO.getCFPImageCustomized() != null) {
                medicalCaseDTO.setCFPImageCustomized(ImageUtility.decompressImage(medicalCaseDTO.getCFPImageCustomized()));
            }
            return medicalCaseDTO;
        }), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<Set<MedicalCaseCustomizedDTO>> getAllMedicalCasesAssignedTo(@RequestParam(value = "encodedInfo") String encodedInfo) {
        Set<MedicalCase> medicalCases = medicalCasesService.getAllMedicalCasesAssignedTo(encodedInfo);
        return new ResponseEntity<>(medicalCases.stream()
                .map(medicalCase -> {
                    MedicalCaseCustomizedDTO medicalCaseCustomizedDTO = medicalCasesCustomizedMapper.toDto(medicalCase);
                    medicalCaseCustomizedDTO.setCFPImage(ImageUtility.decompressImage(medicalCaseCustomizedDTO.getCFPImage()));
                    return medicalCaseCustomizedDTO;
                }).collect(Collectors.toSet()), HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<StringResponseDTO> addMedicalCase(@RequestPart("image") MultipartFile image, @RequestPart("medicalCase") String medicalCaseJSON) throws IOException {
        System.out.println(image);
        ObjectMapper objectMapper = new ObjectMapper();
        MedicalCase medicalCase = objectMapper.readValue(medicalCaseJSON, MedicalCase.class);
        medicalCase.setCFPImage(ImageUtility.compressImage(image.getBytes()));

        MedicalCase returnedMedicalCase = medicalCasesService.addMedicalCase(medicalCase);

        return new ResponseEntity<>(StringResponseDTO.builder().message("The medical case with id " + returnedMedicalCase.getId() + " was added!").build(), HttpStatus.CREATED);
    }

    @PostMapping("/drawing")
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<StringResponseDTO> addDrawing(@RequestPart("image") MultipartFile image, @RequestPart("medicalCase") String medicalCaseJSON) throws IOException {
        System.out.println(image);
        ObjectMapper objectMapper = new ObjectMapper();
        MedicalCase medicalCase = objectMapper.readValue(medicalCaseJSON, MedicalCase.class);
        medicalCase.setCFPImageCustomized(ImageUtility.compressImage(image.getBytes()));

        MedicalCase returnedMedicalCase = medicalCasesService.addDrawingToMedicalCase(medicalCase);

        return new ResponseEntity<>(StringResponseDTO.builder().message("The medical case with id " + returnedMedicalCase.getId() + " was updated!").build(), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_RESIDENT', 'ROLE_EXPERT')")
    public ResponseEntity<MedicalCaseFullDTO> updateMedicalCase(@RequestBody MedicalCaseFullDTO medicalCaseDTO) {
        MedicalCase returnedMedicalCase = medicalCasesService.updateMedicalCase(medicalCasesMapper.toDomain(medicalCaseDTO));
        return new ResponseEntity<>(medicalCasesMapper.toDto(returnedMedicalCase), HttpStatus.OK);
    }

//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_RESIDENT')")
//    public ResponseEntity<MedicalCaseFullDTO> completeMedicalCase(Principal principal, @PathVariable UUID id) {
//        MedicalCase returnedMedicalCase = medicalCasesService.completeMedicalCase(principal.getName(), id);
//        return new ResponseEntity<>(medicalCasesMapper.toDto(returnedMedicalCase), HttpStatus.OK);
//    }

//    @PutMapping("/validate")
//    @PreAuthorize("hasRole('ROLE_RESIDENT')")
//    public ResponseEntity<String> validateMedicalCase(@RequestBody MedicalCaseDTO medicalCaseDTO) {
////        MedicalCase providedMedicalCase = medicalCasesMapper.toDomain(medicalCaseDTO);
////        MedicalCase actualMedicalCase = medicalCasesService.getMedicalCase(providedMedicalCase.getId());
////
////        if (actualMedicalCase == null) {
////            return new ResponseEntity<>("The medical with id: " + providedMedicalCase.getId() + " case can not be found!", HttpStatus.NOT_FOUND);
////        }
////
////        if (providedMedicalCase.getCFPImage() != actualMedicalCase.getCFPImage()) {
////            return new ResponseEntity<>("You can not change the case!", HttpStatus.CONFLICT);
////        }
////
////        MedicalCase returnedMedicalCase = medicalCasesService.updateMedicalCase(providedMedicalCase);
////        if (returnedMedicalCase == null) {
////            return new ResponseEntity<>("The medical case could not be updated!", HttpStatus.CONFLICT);
////        }
//        return new ResponseEntity<>("The medical case was validated!", HttpStatus.OK);
//    }
}
