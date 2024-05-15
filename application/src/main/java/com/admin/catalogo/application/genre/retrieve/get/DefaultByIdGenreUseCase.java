package com.admin.catalogo.application.genre.retrieve.get;

import java.util.Objects;

import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;

public class DefaultByIdGenreUseCase extends GetByIdGenreUseCase {


    
    private final GenreGateway genreGateway;

    public DefaultByIdGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anId) {
        final var aGenreId = GenreID.from(anId);
        return this.genreGateway.findById(aGenreId).map(GenreOutput::from)
            .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }



}
