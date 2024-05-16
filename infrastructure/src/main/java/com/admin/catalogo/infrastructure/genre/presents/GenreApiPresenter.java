package com.admin.catalogo.infrastructure.genre.presents;

import java.util.function.Function;

import com.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.admin.catalogo.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {
    
    Function<GenreOutput, GenreResponse> present =
         output ->  new GenreResponse(
                    output.id(), 
                    output.name(), 
                    output.categories(), 
                    output.isActive(),
                    output.createdAt(), 
                    output.updatedAt(), 
                    output.deletedAt()
                );

    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(), 
                output.name(), 
                output.categories(), 
                output.isActive(), 
                output.createdAt(), 
                output.updatedAt(), 
                output.deletedAt()
            );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
            output.id(), 
            output.name(), 
            output.isActive(), 
            output.createdAt(), 
            output.deletedAt()
            );
    }
}
