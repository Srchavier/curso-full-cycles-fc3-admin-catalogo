package com.admin.catalogo.infrastructure.category.presenters;


import java.util.function.Function;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {
    
    Function<CategoryOutput, CategoryApiOutput> present =
         output ->  new CategoryApiOutput(
                output.categoryID().getValue(), 
                output.name(), 
                output.description(), 
                output.isActive(), 
                output.createdAt(), 
                output.updatedAt(), 
                output.deletedAt());

    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
            output.categoryID().getValue(), 
            output.name(), 
            output.description(), 
            output.isActive(), 
            output.createdAt(), 
            output.updatedAt(), 
            output.deletedAt());
    }
}
