package org.example.mapper;

public interface Converter<E,D>{
    D entityToDto(E e);
    E dtoToEntity(D d);
}