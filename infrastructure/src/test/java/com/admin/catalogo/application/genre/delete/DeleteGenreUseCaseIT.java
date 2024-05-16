package com.admin.catalogo.application.genre.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @SpyBean
    private GenreGateway gatewayGenreGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var aGenre = gatewayGenreGateway.create(Genre.newGenre("filmes", true));
        final var expectedId = aGenre.getId();
        assertEquals(1, genreRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gatewayGenreGateway, times(1)).deleteById(expectedId);

        assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var aGenre = gatewayGenreGateway.create(Genre.newGenre("filmes", true));

        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gatewayGenreGateway, times(1)).deleteById(expectedId);

        assertEquals(1, genreRepository.count());

    }


}
