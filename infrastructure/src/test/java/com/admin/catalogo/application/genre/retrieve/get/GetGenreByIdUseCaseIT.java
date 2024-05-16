package com.admin.catalogo.application.genre.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private DefaultByIdGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {

        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Series", null, true));

        final var expectedName = "name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId());

        final var aGenre = genreGateway.create(Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories));
        final var expectId = aGenre.getId();

        final var actualGenre = useCase.execute(expectId.getValue());

        assertEquals(expectId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(),actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(any());

    }


    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }


    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }

}
