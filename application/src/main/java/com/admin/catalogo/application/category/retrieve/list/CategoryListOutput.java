package com.admin.catalogo.application.category.retrieve.list;

import java.time.Instant;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record CategoryListOutput (
    CategoryID id,
    String name,
    String description,
    Boolean isActive,
    Instant createdAt,
    Instant deleteAt
) {

    public static CategoryListOutput from(final Category aCategory) {
        return new CategoryListOutput(
            aCategory.getId(),
            aCategory.getName(),
            aCategory.getDescription(),
            aCategory.isActive(),
            aCategory.getCreatedAt(),
            aCategory.getDeletedAt()
        );
    }
    
}
