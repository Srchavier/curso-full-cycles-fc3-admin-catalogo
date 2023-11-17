package com.admin.catalogo.infrastructure.category.models;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import com.admin.catalogo.JacksonTest;

@JacksonTest
public class CreateCategoryRequestTest {

    
    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida 2023";
        final var expectedActive = true;

        final var response = new CreateCategoryRequest(
            expectedName,
            expectedDescription,
            expectedActive);

        final var actualJson = json.write(response);

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedActive);

    }
    
    @Test
    public void testUnMarshall() throws IOException {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida 2023";
        final var expectedActive = true;
        
        final var jsonString = """
            {
                "name":"%s",
                "description":"%s",
                "is_active": %s
            }
            """
            .formatted(expectedName, expectedDescription, expectedActive);


         final var actualJson = json.parse(jsonString); 

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedActive);
    }
}
