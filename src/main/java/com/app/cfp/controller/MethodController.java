package com.app.cfp.controller;

import com.app.cfp.dto.MethodDTO;
import com.app.cfp.mapper.MethodMapper;
import com.app.cfp.service.MethodService;
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
@RequestMapping(value = "/methods")
@AllArgsConstructor
@CrossOrigin
public class MethodController {


    private final MethodService MethodService;

    private final MethodMapper MethodMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MethodDTO>> getAllMethods() {
        return new ResponseEntity<>(MethodService.getAllMethods().stream().map(MethodMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
