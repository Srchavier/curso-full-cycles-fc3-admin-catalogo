package com.admin.catalogo.infrastructure.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.admin.catalogo.MySQLGatewayTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.pagination.SearchQuery;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjectedDependencies() {
        assertNotNull(categoryMySQLGateway);
        assertNotNull(categoryRepository);
    }

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescrition = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescrition, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId().getValue(), actualCategory.getId().getValue());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescrition, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryMySQLGateway.findById(aCategory.getId()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId().getValue());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescrition, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());

    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnANewCategoryUpdate() {
        final var expectedName = "Filmes";
        final var expectedDescrition = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("film", null, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var actualInvalidEntity = categoryMySQLGateway.findById(aCategory.getId()).get();
        assertEquals("film", actualInvalidEntity.getName());
        assertNull(actualInvalidEntity.getDescription());
        assertEquals(true, actualInvalidEntity.isActive());

        final var aUpdateCategory = aCategory.clone().update(expectedName, expectedDescrition, expectedIsActive);

        final var actualCategory = categoryMySQLGateway.update(aUpdateCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId().getValue(), actualCategory.getId().getValue());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescrition, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryMySQLGateway.findById(aCategory.getId()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId().getValue());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescrition, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());

    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        final var expectedName = "Filmes";
        final var expectedDescrition = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescrition, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());
    }

    @Test
    public void givenIValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {

        assertEquals(0, categoryRepository.count());

        categoryMySQLGateway.deleteById(CategoryID.from("invalid"));

        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescrition = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescrition, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId().getValue(), actualCategory.getId().getValue());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescrition, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, categoryRepository.count());
        final var actualCategory = categoryMySQLGateway.findById(CategoryID.from("empty"));
        assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPrePage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filems", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");

        final var actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPrePage = 1;
        final var expectedTotal = 0;

        assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");

        final var actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedTotal, actualResult.items().size());
    }

    @Test
    public void givenFollowPaginatione_whenCallsFindAllPage1_shouldReturnPaginated() {

        var expectedPage = 0;
        final var expectedPrePage = 1;
        final var expectedTotal = 3;

        final var filmes = Category.newCategory("Filems", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        assertEquals(3, categoryRepository.count());

        var query = new SearchQuery(0, 1, "", "name", "asc");

        var actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;
        query = new SearchQuery(1, 1, "", "name", "asc");

        actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(filmes.getId(), actualResult.items().get(0).getId());

        // Page 3
        expectedPage = 2;
        query = new SearchQuery(2, 1, "", "name", "asc");

        actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(series.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPrePage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filems", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "doc", "name", "asc");

        final var actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPrePage = 1;
        final var expectedTotal = 1;

        final var filmes = Category.newCategory("Filems", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory("Documentarios", "A categoria menos assistida", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");

        final var actualResult = categoryMySQLGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPrePage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPrePage, actualResult.items().size());
        assertEquals(filmes.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        final var filmes = Category.newCategory("Filems", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "Uma categoria assistida", true);
        final var documentarios = Category.newCategory("Documentarios", "A categoria menos assistida", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(filmes),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentarios)));

        assertEquals(3, categoryRepository.count());

        final var expectedIds = List.of(
            filmes.getId(),
            series.getId());

        final var ids = List.of(
            filmes.getId(),
            series.getId(),
            CategoryID.from("123222"));

        final var actualResult = categoryMySQLGateway.existsByIds(ids);

        assertEquals(expectedIds.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList(), actualResult.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList());

    }
}
