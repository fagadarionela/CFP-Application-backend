package com.app.cfp.mapper;

public interface DataMapper<S, T> {
    T toDto(S data);

    S toDomain(T data);
}
