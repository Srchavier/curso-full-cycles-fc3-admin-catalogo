package com.admin.catalogo.application.category;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.admin.catalogo.IntegrationTest;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void testInjects() {
        assertNotNull(categoryRepository);
        assertNotNull(useCase);
    }
    
}
