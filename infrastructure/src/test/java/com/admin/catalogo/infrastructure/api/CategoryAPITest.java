package com.admin.catalogo.infrastructure.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vavr.API;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper mapper;

        @MockBean
        private CreateCategoryUseCase createCategoryUseCase;

        @MockBean
        private GetCategoryByIdUseCase getCategoryByIdUseCase;

        @MockBean
        private UpdateCategoryUseCase updateCategoryUseCase;

        @MockBean
        private DeleteCategoryUseCase deleteCategoryUseCase;

        @MockBean
        private ListCategoriesUseCase listCategoriesUseCase;

        @Test
        public void givenValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                when(createCategoryUseCase.execute(any()))
                                .thenReturn(API.Right(CreateCategoryOutput.from("123")));

                final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aInput));

                this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print())
                                .andExpectAll(
                                                MockMvcResultMatchers.status().isCreated(),
                                                MockMvcResultMatchers.header().string("Location", "/categories/123"),
                                                MockMvcResultMatchers.header().string("Content-Type",
                                                                MediaType.APPLICATION_JSON_VALUE),
                                                MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123")));

                verify(createCategoryUseCase, times(1))
                                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription, cmd.description())
                                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
                final String expectedName = null;
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedMessage = "'name' should not be null";

                when(createCategoryUseCase.execute(any()))
                                .thenReturn(API.Left(Notification.create(
                                                new com.admin.catalogo.domain.validation.Error(expectedMessage))));

                final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
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
                                                MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                                Matchers.equalTo(expectedMessage))

                                );

                verify(createCategoryUseCase, times(1))
                                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription, cmd.description())
                                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainExceptiond() throws Exception {
                final String expectedName = null;
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var expectedMessage = "'name' should not be null";

                when(createCategoryUseCase.execute(any()))
                                .thenThrow(NotFoundException
                                                .with(new com.admin.catalogo.domain.validation.Error(expectedMessage)));

                final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aInput));

                this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print())
                                .andExpectAll(
                                                MockMvcResultMatchers.status().isUnprocessableEntity(),
                                                MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                                                MockMvcResultMatchers.header().string("Content-Type",
                                                                MediaType.APPLICATION_JSON_VALUE),
                                                MockMvcResultMatchers.jsonPath("$.message",
                                                                Matchers.equalTo(expectedMessage)),
                                                MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)),
                                                MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                                Matchers.equalTo(expectedMessage))

                                );

                verify(createCategoryUseCase, times(1))
                                .execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                                && Objects.equals(expectedDescription, cmd.description())
                                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

                final var expectedId = aCategory.getId().getValue();

                when(getCategoryByIdUseCase.execute(any()))
                                .thenReturn(CategoryOutput.from(aCategory));

                // WHEN
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories/{id}", expectedId);

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.description",
                                                Matchers.equalTo(expectedDescription)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active",
                                                Matchers.equalTo(expectedIsActive)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at",
                                                Matchers.equalTo(aCategory.getCreatedAt().toString())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at",
                                                Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at",
                                                Matchers.equalTo(aCategory.getDeletedAt())));

                verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));

        }

        @Test
        public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
                // given
                final var expectedId = CategoryID.from("123");
                final var expectedErrorMessage = "Category with ID 123 was not found";

                when(getCategoryByIdUseCase.execute(any()))
                                .thenThrow(NotFoundException.with(Category.class, expectedId));

                // when
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories/{id}", expectedId);

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                Matchers.equalTo(expectedErrorMessage)));
        }

        @Test
        public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
                // given
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                when(updateCategoryUseCase.execute(any()))
                                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

                final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                // WHEN
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

                verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenACommandWithInvalidID_whenGatewayUpdateCategory_shouldReturnNotFoundException()
                        throws Exception {
                // given
                final var expectedId = "not-found";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var expectedErrorMessage = "Category with ID not-found was not found";

                when(updateCategoryUseCase.execute(any()))
                                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

                final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                // WHEN

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                Matchers.equalTo(expectedErrorMessage)));
                ;

                verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
                // given
                final var expectedId = "123";
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var expectedMessage = "'name' should not be null";

                when(updateCategoryUseCase.execute(any()))
                                .thenReturn(API.Left(Notification.create(
                                                new com.admin.catalogo.domain.validation.Error(expectedMessage))));

                final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                // WHEN

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message",
                                                Matchers.equalTo(expectedMessage)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)));
                ;

                verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())));

        }

        @Test
        public void givenAValidId_whenCallsDeleteCategory_shouldNoContent() throws Exception {
                // given
                final var expectedId = "123";

                doNothing().when(deleteCategoryUseCase).execute(any());
                ;

                // WHEN
                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId);

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response.andExpect(MockMvcResultMatchers.status().isNoContent());

                verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));

        }

        @Test
        public void givenValidParams_whenCallsListCategories_thenShouldReturnCategories() throws Exception {
                // given
                final var aCategory = Category.newCategory("movies", null, true);
                final var expectedPage = 0;
                final var expectedPerPage = 10;
                final var expectedTerms = "movies";
                final var expectedSort = "description";
                final var expectedDirecion = "desc";
                final var expectedItemsCount = 1;
                final var expectedTotal = 1;
                final var expectedItems = List.of(CategoryListOutput.from(aCategory));

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                when(listCategoriesUseCase.execute(any()))
                                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal,
                                                expectedItems));

                final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                // WHEN

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories")
                                .queryParam("page", String.valueOf(expectedPage))
                                .queryParam("perPage", String.valueOf(expectedPerPage))
                                .queryParam("sort", expectedSort)
                                .queryParam("dir", expectedDirecion)
                                .queryParam("search", expectedTerms)

                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(request)
                                .andDo(MockMvcResultHandlers.print());

                // then
                response
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.header().string("Content-Type",
                                                MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.isActive())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));


                verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                                Objects.equals(expectedPage, query.page())
                                && Objects.equals(expectedPerPage, query.perPage())
                                && Objects.equals(expectedTerms, query.terms())
                                && Objects.equals(expectedSort, query.sort())
                                && Objects.equals(expectedDirecion, query.direction())
                                ));

        }
}
