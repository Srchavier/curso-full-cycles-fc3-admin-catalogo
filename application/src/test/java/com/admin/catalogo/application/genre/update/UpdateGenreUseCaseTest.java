package com.admin.catalogo.application.genre.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }
    

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(aGenre)));
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        

        final var actualOutput = useCase.execute(aCommand);



        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));


        Mockito.verify(genreGateway, times(1))
                .update(Mockito.argThat(aUpdatedGenre -> 
                         Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.getActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())));

    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(aGenre)));
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand);



        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));


        Mockito.verify(genreGateway, times(1))
                .update(Mockito.argThat(aUpdatedGenre -> 
                         Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.getActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(aUpdatedGenre.getDeletedAt())));

    }

    @Test
    void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {

        final var aGenre = Genre.newGenre("acao", true);
        final var films = CategoryID.from("123");
        final var series = CategoryID.from("1234");
        final var doc = CategoryID.from("12345");

        final var expectedId = aGenre.getId();
        final var expectedName = "name";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(films, series, doc);

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(aGenre)));
        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

    

        final var actualOutput = useCase.execute(aCommand);



        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));


        Mockito.verify(genreGateway, times(1))
                .update(Mockito.argThat(aUpdatedGenre -> 
                         Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.getActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())));

    }

    @Test
    void givenAInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {

        final var aGenre = Genre.newGenre("acao", true);

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        // Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);
        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(aGenre)));

    
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

        final var aGenre = Genre.newGenre("acao", true);
        final var films = CategoryID.from("123");
        final var series = CategoryID.from("1234");
        final var doc = CategoryID.from("12345");

        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(films, series, doc);
        final var expectedErrorMessage = "Some categories could not be found: 1234, 12345";
        final var expectedErrorMessage2 = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(expectedId.getValue(), expectedName, expectedIsActive, asString(expectedCategories));

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(films));
        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(Genre.with(aGenre)));

    
        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));


        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());
        assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).getMessage());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());

    }


}
