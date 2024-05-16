package com.admin.catalogo.infrastructure.api.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.get.GetByIdGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.infrastructure.api.GenreAPI;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.admin.catalogo.infrastructure.genre.presents.GenreApiPresenter;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;

    private final GetByIdGenreUseCase byIdGenreUseCase;

    private final UpdateGenreUseCase updateGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    private final ListGenreUseCase listGenreUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase, GetByIdGenreUseCase byIdGenreUseCase,
            UpdateGenreUseCase updateGenreUseCase, DeleteGenreUseCase deleteGenreUseCase,
            ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.byIdGenreUseCase = byIdGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aGenreCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories());

        final var output = this.createGenreUseCase.execute(aGenreCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final String search, final int page, final int perpage, final String sort,
            final String direction) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perpage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.byIdGenreUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aGenreCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.isActive(),
                input.categories());

        final var output = this.updateGenreUseCase.execute(aGenreCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }

}
