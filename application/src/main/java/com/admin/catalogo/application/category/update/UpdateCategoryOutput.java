package com.admin.catalogo.application.category.update;

import com.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
    CategoryID id
) {

    public static UpdateCategoryOutput from (final CategoryID id) {
        return new UpdateCategoryOutput(id);
    }
    
}
