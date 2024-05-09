package com.admin.catalogo.application.genre.retrieve.list;

import java.time.Instant;
import java.util.List;

import com.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.Genre;

public record GenreListOutput(
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) {

    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getName(),
                aGenre.getActive(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
                );
    }
}
