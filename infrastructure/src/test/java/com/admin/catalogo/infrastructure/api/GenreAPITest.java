package com.admin.catalogo.infrastructure.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.admin.catalogo.application.genre.retrieve.get.GetByIdGenreUseCase;
import com.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vavr.API;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @MockBean
    private GetByIdGenreUseCase byIdGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @Test
    public void givenValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("Location", "/genres/" + expectedId),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(createGenreUseCase, times(1))
                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));

    }

    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErroMessage = "'name' should not be null";

        final var aInput = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErroMessage))));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErroMessage)));

        verify(createGenreUseCase, times(1))
                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        final String expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final List<CategoryID> categories = expectedCategories
                .stream()
                .map(t -> CategoryID.from(t))
                .toList();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(categories);

        final var expectedId = aGenre.getId().getValue();

        when(byIdGenreUseCase.execute(expectedId))
                .thenReturn(GenreOutput.from(aGenre));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)),
                        MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)),
                        MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)),
                        MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)),
                        MockMvcResultMatchers.jsonPath("$.created_at",
                                Matchers.equalTo(aGenre.getCreatedAt().toString())),
                        MockMvcResultMatchers.jsonPath("$.updated_at",
                                Matchers.equalTo(aGenre.getUpdatedAt().toString())),
                        MockMvcResultMatchers.jsonPath("$.deleted_at",
                                Matchers.equalTo(aGenre.getDeletedAt().toString())));

        verify(byIdGenreUseCase, times(1))
                .execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        final String expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(byIdGenreUseCase.execute(expectedId.getValue()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isNotFound(),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        verify(byIdGenreUseCase, times(1))
                .execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final List<CategoryID> categories = expectedCategories
                .stream()
                .map(t -> CategoryID.from(t))
                .toList();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(categories);

        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(aGenre));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(updateGenreUseCase, times(1))
                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));

    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErroMessage = "'name' should not be null";

        final List<CategoryID> categories = expectedCategories
                .stream()
                .map(t -> CategoryID.from(t))
                .toList();

        final var aGenre = Genre.newGenre("Açào", expectedIsActive)
                .addCategories(categories);

        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErroMessage))));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.header().string("Content-Type",
                                MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErroMessage)));

        verify(updateGenreUseCase, times(1))
                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    public void givenAnValidId_whenCallsDeleteGenre_shouldBeOK() throws Exception {

        final var expectedId = "123";

        doNothing().when(deleteGenreUseCase).execute(expectedId);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isNoContent());

        verify(deleteGenreUseCase).execute(eq(expectedId));

    }

    @Test
    public void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {

        final var aGenre = Genre.newGenre("ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("current_page", Matchers.equalTo(expectedPage)),
                        MockMvcResultMatchers.jsonPath("per_page", Matchers.equalTo(expectedPerPage)),
                        MockMvcResultMatchers.jsonPath("total", Matchers.equalTo(expectedTotal)),
                        MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedCount)),
                        MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aGenre.getId().getValue())),
                        MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aGenre.getName())),
                        MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aGenre.getActive())),
                        MockMvcResultMatchers.jsonPath("$.items[0].created_at",
                                Matchers.equalTo(aGenre.getCreatedAt().toString())),
                        MockMvcResultMatchers.jsonPath("$.items[0].deleted_at",
                                Matchers.equalTo(aGenre.getDeletedAt().toString())));

        verify(listGenreUseCase, times(1))
                .execute(argThat(cmd -> Objects.equals(expectedPage, cmd.page())
                        && Objects.equals(expectedPerPage, cmd.perPage())
                        && Objects.equals(expectedDirection, cmd.direction())
                        && Objects.equals(expectedSort, cmd.sort())
                        && Objects.equals(expectedTerms, cmd.terms())));

    }

}
