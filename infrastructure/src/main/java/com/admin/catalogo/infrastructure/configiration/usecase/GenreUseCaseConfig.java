package com.admin.catalogo.infrastructure.configiration.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.admin.catalogo.application.genre.delete.DefaultDeleteUseCase;
import com.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.get.GetByIdGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.get.DefaultByIdGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.genre.GenreGateway;

@Component
public class GenreUseCaseConfig {
    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(final GenreGateway GenreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(GenreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(categoryGateway, genreGateway);
    }

    @Bean
    public GetByIdGenreUseCase getGenreByIdUseCase() {
        return new DefaultByIdGenreUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenresUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteUseCase(genreGateway);
    }
}
