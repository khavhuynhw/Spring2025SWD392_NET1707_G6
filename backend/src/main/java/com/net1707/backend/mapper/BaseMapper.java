package com.net1707.backend.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class BaseMapper<D,E> {
    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);

    List<E> toEntities(List<D> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    List<D> toDTOs(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
