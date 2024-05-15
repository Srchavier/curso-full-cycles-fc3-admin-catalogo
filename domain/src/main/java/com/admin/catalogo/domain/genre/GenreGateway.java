package com.admin.catalogo.domain.genre;

import java.util.List;
import java.util.Optional;

import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(GenreID genreID);

    Optional<Genre> findById(GenreID genreID);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);

    List<GenreID> existsByIds(final Iterable<GenreID> genreIDS);

}
