package com.app.cfp.mapper;

import org.springframework.context.annotation.Configuration;


public interface DataMapper <S, T> {
    T toDto(S data);

    S toDomain(T data);
}
