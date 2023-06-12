package com.admin.catalogo.application.category.update;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
    CategoryID id
) {

    public static UpdateCategoryOutput from (final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
    
}
