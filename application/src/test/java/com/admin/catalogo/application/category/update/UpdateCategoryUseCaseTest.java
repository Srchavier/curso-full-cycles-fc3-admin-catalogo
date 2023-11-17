package com.admin.catalogo.application.category.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade inválida (name)
    // 3. Teste atualizando uma categoria para inativa
    // 4. Teste simulando um erro generico vindo do gateway
    // 5. Teste atualizar categoria passando ID inválido

    @Test
    public void givenValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription, 
            expectedIsActive
            );

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(Category.with(aCategory)));

        when(categoryGateway.update(any())).thenReturn(aCategory);


        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());


        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1))
                .update(argThat(updateCategory -> {

                    return Objects.equals(expectedName, updateCategory.getName())
                            && Objects.equals(expectedDescription, updateCategory.getDescription())
                            && Objects.equals(expectedIsActive, updateCategory.isActive())
                            && Objects.equals(expectedId, updateCategory.getId())
                            && Objects.equals(aCategory.getCreatedAt(), updateCategory.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(updateCategory.getUpdatedAt())
                            && Objects.isNull(updateCategory.getDeletedAt());
                }));
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
        
        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName,expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(Category.with(aCategory)));

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
            expectedIsActive
            );

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(Category.with(aCategory)));

        when(categoryGateway.update(any())).thenReturn(aCategory);

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());


        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1))
                .update(argThat(updateCategory -> {

                    return Objects.equals(expectedName, updateCategory.getName())
                            && Objects.equals(expectedDescription, updateCategory.getDescription())
                            && Objects.equals(expectedIsActive, updateCategory.isActive())
                            && Objects.equals(expectedId, updateCategory.getId())
                            && Objects.equals(aCategory.getCreatedAt(), updateCategory.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(updateCategory.getUpdatedAt())
                            && Objects.nonNull(updateCategory.getDeletedAt());
                }));

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
            expectedIsActive
            );

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(Category.with(aCategory)));

        when(categoryGateway.update(any()))
            .thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();
        
        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstError().getMessage());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
       
        verify(categoryGateway, times(1))
                .update(argThat(aUpdateCategory -> {

                    return Objects.equals(expectedName, aUpdateCategory.getName())
                            && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                            && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                            && Objects.equals(expectedId, aUpdateCategory.getId())
                            && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                            && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                            && Objects.isNull(aUpdateCategory.getDeletedAt());
                }));
        }
       
        @Test
        public void givenACommandWithInvalidID_whenGatewayUpdateCategory_shouldReturnNotFoundException() {
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;
            final var expectedId = "123";
    
            final var expectedErrorMessage = "Category with ID 123 was not found";
            // final var expectedErrorCount = 1;
    
            final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription, 
                expectedIsActive
                );
    
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

    
            final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

            // assertEquals(expectedErrorCount, actualException.getErrors().size());
            assertEquals(expectedErrorMessage, actualException.getMessage());

            verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));

            verify(categoryGateway, times(0))
                    .update(any());
            }
}
