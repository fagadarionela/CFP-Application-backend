package com.app.cfp.service;

import com.app.cfp.entity.Method;
import com.app.cfp.repository.MethodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MethodService {
    private final MethodRepository MethodRepository;
    public List<Method> getAllMethods(){
        return MethodRepository.findAll();
    }
}
