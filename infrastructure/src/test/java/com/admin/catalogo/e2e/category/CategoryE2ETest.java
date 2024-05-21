package com.admin.catalogo.e2e.category;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.admin.catalogo.E2ETest;
import com.admin.catalogo.e2e.MockDsl;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CategoryRepository categoryRepository;

        @SuppressWarnings("rawtypes")
        @Container
        private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0.26")
                        .withPassword("123456")
                        .withUsername("root")
                        .withDatabaseName("adm_videos");

        @DynamicPropertySource
        public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
                registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
        }

        @Override
        public MockMvc mvc() {
                return this.mockMvc;
        }

        @Test
        public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                assertEquals(actualId.getValue(), actualCategory.getId());
                assertEquals(expectedName, actualCategory.getName());
                assertEquals(expectedDescription, actualCategory.getDescription());
                assertEquals(expectedIsActive, actualCategory.getActive());
                assertNotNull(actualCategory.getCreatedAt());
                assertNotNull(actualCategory.getUpdatedAt());
                assertNull(actualCategory.getDeletedAt());

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {

                assertEquals(0, categoryRepository.count());

                givenACategory("Filmes", "A categoria mais assistida", true);
                givenACategory("Documentarios", "A categoria mais Documentarios", true);
                givenACategory("Series", "A categoria mais series", true);

                listCategories(0, 1)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Documentarios")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description",
                                                Matchers.equalTo("A categoria mais Documentarios")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active",
                                                Matchers.equalTo(true)));

                listCategories(1, 1)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Filmes")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description",
                                                Matchers.equalTo("A categoria mais assistida")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active",
                                                Matchers.equalTo(true)));

                listCategories(2, 1)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Series")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description",
                                                Matchers.equalTo("A categoria mais series")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active",
                                                Matchers.equalTo(true)));

                listCategories(3, 1)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {

                assertEquals(0, categoryRepository.count());

                givenACategory("Filmes", "A categoria mais assistida", true);
                givenACategory("Documentarios", "A categoria mais Documentarios", true);
                givenACategory("Series", "A categoria mais series", true);

                listCategories(0, 1, "filme")
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Filmes")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description",
                                                Matchers.equalTo("A categoria mais assistida")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active",
                                                Matchers.equalTo(true)));

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToSortBetweenAllCategoriesByDescriptionDesc() throws Exception {

                assertEquals(0, categoryRepository.count());

                givenACategory("Filmes", "C categoria mais Filmes", true);
                givenACategory("Documentarios", "Z categoria mais Documentarios", true);
                givenACategory("Series", "A categoria mais series", true);

                listCategories(0, 3, "", "description", "desc")
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name",
                                                Matchers.equalTo("Documentarios")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name",
                                                Matchers.equalTo("Filmes")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name",
                                                Matchers.equalTo("Series")));

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

                final var actualCategory = retrieveACategory(actualId);

                assertEquals(actualId.getValue(), actualCategory.id());
                assertEquals(expectedName, actualCategory.name());
                assertEquals(expectedDescription, actualCategory.description());
                assertEquals(expectedIsActive, actualCategory.active());
                assertNotNull(actualCategory.createdAt());
                assertNotNull(actualCategory.updatedAt());
                assertNull(actualCategory.deletedAt());

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToGSeeATreatedErrorByGettingANotFoundCategory() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var aRequest = MockMvcRequestBuilders.get("/categories/123")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON);

                this.mockMvc.perform(aRequest)
                                .andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                                                Matchers.equalTo("Category with ID 123 was not found")));

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var actualId = givenACategory("Movies", "", true);

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                updateACategory(actualId, requestBody).andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                assertEquals(actualId.getValue(), actualCategory.getId());
                assertEquals(expectedName, actualCategory.getName());
                assertEquals(expectedDescription, actualCategory.getDescription());
                assertEquals(expectedIsActive, actualCategory.getActive());
                assertNotNull(actualCategory.getCreatedAt());
                assertNotNull(actualCategory.getUpdatedAt());
                assertNull(actualCategory.getDeletedAt());

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToInactiveACategoryByItsIdentifier() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = false;

                final var actualId = givenACategory(expectedName, expectedDescription, true);

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                updateACategory(actualId, requestBody)
                                .andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                assertEquals(actualId.getValue(), actualCategory.getId());
                assertEquals(expectedName, actualCategory.getName());
                assertEquals(expectedDescription, actualCategory.getDescription());
                assertEquals(expectedIsActive, actualCategory.getActive());
                assertNotNull(actualCategory.getCreatedAt());
                assertNotNull(actualCategory.getUpdatedAt());
                assertNotNull(actualCategory.getDeletedAt());

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToActiveACategoryByItsIdentifier() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;

                final var actualId = givenACategory(expectedName, expectedDescription, false);

                final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

                updateACategory(actualId, requestBody)
                                .andExpect(MockMvcResultMatchers.status().isOk());

                final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

                assertEquals(actualId.getValue(), actualCategory.getId());
                assertEquals(expectedName, actualCategory.getName());
                assertEquals(expectedDescription, actualCategory.getDescription());
                assertEquals(expectedIsActive, actualCategory.getActive());
                assertNotNull(actualCategory.getCreatedAt());
                assertNotNull(actualCategory.getUpdatedAt());
                assertNull(actualCategory.getDeletedAt());

        }

        @Test
        public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {

                assertEquals(0, categoryRepository.count());

                final var actualId = givenACategory("Filmes", null, true);

                deleteACategory(actualId)
                                .andExpect(MockMvcResultMatchers.status().isNoContent());

                assertFalse(this.categoryRepository.existsById(actualId.getValue()));

        }

}
