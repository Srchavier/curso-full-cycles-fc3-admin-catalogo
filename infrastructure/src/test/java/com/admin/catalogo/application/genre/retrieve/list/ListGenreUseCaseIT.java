package com.admin.catalogo.application.genre.retrieve.list;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.pagination.SearchQuery;

@IntegrationTest
public class ListGenreUseCaseIT {

        @Autowired
        private ListGenreUseCase useCase;

        @SpyBean
        private GenreGateway genreGateway;

        @Test
        public void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {

                final var acao = genreGateway.create(Genre.newGenre("acao", true));
                genreGateway.create(Genre.newGenre("comedy", true));
                final var ficcao = genreGateway.create(Genre.newGenre("ficcao", true));

                final var genres = List.of(
                                acao,
                                ficcao);

                final var expectedPage = 0;
                final var expectedPerPage = 10;
                final var expectedTerms = "A";
                final var expectedSort = "createdAt";
                final var expectedDirection = "asc";
                final var expectedTotal = 2;

                final var expectedItems = genres.stream()
                                .map(GenreListOutput::from)
                                .toList();

                final var search = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                                expectedDirection);

                final var actualOutput = useCase.execute(search);

                assertEquals(expectedPage, actualOutput.currentPage());
                assertEquals(expectedPerPage, actualOutput.perPage());
                assertEquals(expectedTotal, actualOutput.total());
                assertEquals(expectedItems.stream().sorted(Comparator.comparing(GenreListOutput::name)).toList(), actualOutput.items().stream().sorted(Comparator.comparing(GenreListOutput::name)).toList());

                verify(genreGateway, times(1)).findAll(eq(search));

        }

        @Test
        public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
                final var genres = List.<Genre>of();

                final var expectedPage = 0;
                final var expectedPerPage = 10;
                final var expectedTerms = "A";
                final var expectedSort = "createdAt";
                final var expectedDirection = "asc";
                final var expectedTotal = 0;

                final var expectedItems = List.<Genre>of();

                final var search = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                                expectedDirection);

                final var actualOutput = useCase.execute(search);

                assertEquals(expectedPage, actualOutput.currentPage());
                assertEquals(expectedPerPage, actualOutput.perPage());
                assertEquals(expectedTotal, actualOutput.total());
                assertEquals(expectedItems, actualOutput.items());
        }

}
