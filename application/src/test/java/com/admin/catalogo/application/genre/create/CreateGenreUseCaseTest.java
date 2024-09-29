package com.admin.catalogo.application.genre.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.GenreGateway;

public class CreateGenreUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }
    

    @Test
    void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "name";
        final var expectedActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedActive, aGenre.getActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())));

    }

    @Test
    void givenAValidCommandWithInactive_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "name";
        final var expectedActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedActive, aGenre.getActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())));

    }

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "name";
        final var expectedActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("1234"));

        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        Mockito.verify(genreGateway, times(1))
                .create(Mockito.argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                        && Objects.equals(expectedActive, aGenre.getActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())));

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
    void givenAInValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var films = CategoryID.from("123");
        final var series = CategoryID.from("1234");
        final var doc = CategoryID.from("12345");


        final String expectedName = "ACAO";
        final var expectedActive = true;
        final var expectedCategories = List.of(
            films,
            series,
            doc
            );

        final var expectedErrorMessage = "Some categories could not be found: 1234, 12345";
        final var expectedErrorCount = 1;

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(films));


        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());


    }

    @Test
    void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        final var films = CategoryID.from("123");
        final var series = CategoryID.from("1234");
        final var doc = CategoryID.from("12345");


        final String expectedName = " ";
        final var expectedActive = true;
        final var expectedCategories = List.of(
            films,
            series,
            doc
            );

        final var expectedErrorMessage = "Some categories could not be found: 1234, 12345";
        final var expectedErrorMessage2 = "'name' should not be black";

        final var expectedErrorCount = 2;

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(films));


        final var aCommand = CreateGenreCommand.with(expectedName, expectedActive, asString(expectedCategories));

        final var actualException = assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());
        assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).getMessage());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());


    }

}
