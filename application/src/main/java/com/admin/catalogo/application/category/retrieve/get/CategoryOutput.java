package com.admin.catalogo.application.category.retrieve.get;

import java.time.Instant;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record CategoryOutput(
    CategoryID categoryID,
    String name, 
    String description,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
    public static CategoryOutput from(final Category aCategory) {
        return new CategoryOutput(aCategory.getId(), aCategory.getName(), aCategory.getDescription(), aCategory.isActive(), aCategory.getCreatedAt(), aCategory.getUpdatedAt(), aCategory.getDeletedAt());
    }
}
