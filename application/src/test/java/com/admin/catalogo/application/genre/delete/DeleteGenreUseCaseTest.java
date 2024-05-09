package com.admin.catalogo.application.genre.delete;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;

public class DeleteGenreUseCaseTest extends UseCaseTest{

    @InjectMocks
    private DefaultDeleteUseCase useCase;

    @Mock
    private GenreGateway gatewayGenreGateway;

    @Override
    public List<Object> getMocks() {
       return List.of(gatewayGenreGateway);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();


        doNothing().when(gatewayGenreGateway).deleteById(expectedId);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gatewayGenreGateway, times(1)).deleteById(expectedId);


    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from("123");

        doNothing().when(gatewayGenreGateway).deleteById(any());

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gatewayGenreGateway, times(1)).deleteById(expectedId);


    }

    @Test
    public void givenInvalidGenreId_whenCallsDeleteGenreAndThrowsUnexpectedError_shouldReceiveException() {
        final var aGenre = Genre.newGenre("acao", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway error")).when(gatewayGenreGateway).deleteById(any());;

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(gatewayGenreGateway, times(1)).deleteById(expectedId);


    }

}
