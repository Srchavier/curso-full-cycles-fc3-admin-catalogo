package com.admin.catalogo.application.genre.retrieve.list;

import java.util.Objects;

import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

public class DefaultListGenreUseCase extends ListGenreUseCase {


    private final GenreGateway genreGateway;

    public DefaultListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery aQuery) {
        return genreGateway.findAll(aQuery).map(GenreListOutput::from);
    }

}
