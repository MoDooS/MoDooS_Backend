package com.study.modoos.common.mapper;

import java.util.List;

public interface GenericMapper <D, E>{
    D toDto(E e);
    E toEntity(D d);
    List<D> toDtoList(List<E> entityList);
    List<E> toEntityList(List<D> dtoList);
}
