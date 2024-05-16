package com.admin.catalogo.application.genre.create;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
public class CreateGenreUseCaseIT{

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "name";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "name";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "name";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAInValidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        final var expectedName = "";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be black";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    
    @Test
    void givenAInValidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        final String expectedName = null;
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }


    @Test
    void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var series = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var films = CategoryID.from("123");
        final var doc = CategoryID.from("12345");

        final String expectedName = " ";
        final var expectedActive = true;
        final var expectedCategories = List.of(
            films,
            series.getId(),
            doc
            );

        final var expectedErrorMessage = "Some categories could not be found: 123, 12345";
        final var expectedErrorMessage2 = "'name' should not be black";

        final var expectedErrorCount = 2;

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());
        assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).getMessage());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());


    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }

        private List<CategoryID> sorted(List<CategoryID> categoryIDs) {
        return categoryIDs.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }


}
