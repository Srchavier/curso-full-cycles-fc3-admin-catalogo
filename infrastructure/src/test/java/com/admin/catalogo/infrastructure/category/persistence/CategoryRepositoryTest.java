package com.admin.catalogo.infrastructure.category.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.admin.catalogo.MySQLGatewayTest;
import com.admin.catalogo.domain.category.Category;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;


    @Test
    public void givenAnInvalidName_whenCallsSave_shouldReturnError() {
        final var propertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var aCategory = Category.newCategory("Filmes", "Filmes mais assistidos", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());


    }

    @Test
    public void givenAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
        final var propertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var aCategory = Category.newCategory("Filmes", "Filmes mais assistidos", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());


    }

    @Test
    public void givenAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
        final var propertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var aCategory = Category.newCategory("Filmes", "Filmes mais assistidos", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> repository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(propertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());


    }
    
}
