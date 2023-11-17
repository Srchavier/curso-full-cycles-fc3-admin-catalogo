package com.admin.catalogo.infrastructure.category.presenters;


import java.util.function.Function;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    
    Function<CategoryOutput, CategoryResponse> present =
         output ->  new CategoryResponse(
                output.categoryID().getValue(), 
                output.name(), 
                output.description(), 
                output.isActive(), 
                output.createdAt(), 
                output.updatedAt(), 
                output.deletedAt());

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
            output.categoryID().getValue(), 
            output.name(), 
            output.description(), 
            output.isActive(), 
            output.createdAt(), 
            output.updatedAt(), 
            output.deletedAt());
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
            output.id().getValue(), 
            output.name(), 
            output.description(), 
            output.isActive(), 
            output.createdAt(), 
            output.deleteAt());
    }
}
