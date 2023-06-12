package com.admin.catalogo.application.category.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        save(aCategory);

        final var expectedId = aCategory.getId();

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(expectedId, actualCategory.categoryID());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());

    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findById(expectedId);

        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Gateway error";
        
        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findById(expectedId);


        final var actualException = assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }

    private void save(final Category... aCategory) {
        var aCategoriesJpa = List.of(aCategory).stream().map(CategoryJpaEntity::from).toList();
        categoryRepository.saveAllAndFlush(aCategoriesJpa);
    }
}
