package com.admin.catalogo.application.category.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exception.DomainException;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("film", null, true);

        save(aCategory);

        assertEquals(1, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        useCase.execute(aCommand);

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        save(aCategory);

        assertEquals(1, categoryRepository.count());

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription,
                expectedIsActive);

        // when(categoryGateway.findById(eq(expectedId)))
        // .thenReturn(Optional.of(Category.with(aCategory)));

        // when(categoryGateway.update(any())).thenReturn(expectedId);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().getMessage());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(0)).update(any());

    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_thenShouldInactiveCategoryId() {
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        save(aCategory);

        assertEquals(1, categoryRepository.count());

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedId.getValue(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNotNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        save(aCategory);

        assertEquals(1, categoryRepository.count());

        doThrow(new IllegalArgumentException(expectedErrorMessage)).when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().getMessage());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(aCategory.getId().getValue(), actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenGatewayUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).getMessage());
    }

    private void save(final Category... aCategory) {
        var aCategoriesJpa = List.of(aCategory).stream().map(CategoryJpaEntity::from).toList();
        categoryRepository.saveAllAndFlush(aCategoriesJpa);
    }
}
