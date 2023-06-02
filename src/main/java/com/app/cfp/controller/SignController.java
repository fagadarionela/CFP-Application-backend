package com.app.cfp.controller;

import com.app.cfp.dto.SignDTO;
import com.app.cfp.mapper.SignMapper;
import com.app.cfp.service.SignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/signs")
@AllArgsConstructor
@CrossOrigin
public class SignController {

    private final SignService SignService;

    private final SignMapper SignMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SignDTO>> getAllSigns() {
        return new ResponseEntity<>(SignService.getAllSigns().stream().map(SignMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
