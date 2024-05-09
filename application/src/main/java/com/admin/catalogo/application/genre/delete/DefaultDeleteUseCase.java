package com.admin.catalogo.application.genre.delete;

import java.util.Objects;

import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;

public class DefaultDeleteUseCase extends DeleteGenreUseCase {


    private final GenreGateway genreGateway;

    public DefaultDeleteUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(String anIn) {
        this.genreGateway.deleteById(GenreID.from(anIn));
    }

}
