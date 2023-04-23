package com.app.cfp.service;

import com.app.cfp.entity.Sign;
import com.app.cfp.repository.SignRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SignService {
    private final SignRepository SignRepository;
    public List<Sign> getAllSigns(){
        return SignRepository.findAll();
    }
}
