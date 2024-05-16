package com.admin.catalogo.application.genre.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());


        final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand);


        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));


        final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var films = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var doc = categoryGateway.create(Category.newCategory("filmes", null, true));

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));


        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(films.getId(), series.getId(), doc.getId());

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());


        final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(actualOutput.id(), actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategoryIDs()));
        assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());

        verify(genreGateway, times(1)).findById(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());

    }
    
    @Test
    void givenAInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {

        final var aGenre = genreGateway.create(Genre.newGenre("acao", true));
        final var films = categoryGateway.create(Category.newCategory("filmes", null, true));
        final var series = CategoryID.from("1234");
        final var doc = CategoryID.from("12345");

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(films.getId(), series, doc);
        final var expectedErrorMessage = "Some categories could not be found: 1234, 12345";
        final var expectedErrorMessage2 = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));
    
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());
        assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).getMessage());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());

    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }

    private List<CategoryID> sorted(List<CategoryID> categoryIDs) {
        return categoryIDs.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
    }

}
