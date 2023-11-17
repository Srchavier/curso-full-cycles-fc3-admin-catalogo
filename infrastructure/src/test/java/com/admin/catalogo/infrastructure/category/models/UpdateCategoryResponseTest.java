package com.admin.catalogo.infrastructure.category.models;

import java.io.IOException;
import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import com.admin.catalogo.JacksonTest;

@JacksonTest
public class UpdateCategoryResponseTest {
    
    @Autowired
    private JacksonTester<UpdateCategoryResponse> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida 2023";
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();

        final var response = new UpdateCategoryResponse(
            expectedId,
            expectedName,
            expectedDescription,
            expectedActive,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeleteAt);

        final var actualJson = json.write(response);

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedActive)
            .hasJsonPathValue("$.updated_at", expectedCreatedAt)
            .hasJsonPathValue("$.created_at", expectedUpdatedAt)
            .hasJsonPathValue("$.deleted_at", expectedDeleteAt);

    }
    
    @Test
    public void testUnMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida 2023";
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();
        
        final var jsonString = """
            {
                "id":"%s",
                "name":"%s",
                "description":"%s",
                "is_active": %s,
                "created_at":"%s",
                "updated_at":"%s",
                "deleted_at":"%s"
            }
            """
            .formatted(expectedId, expectedName, expectedDescription, expectedActive, expectedCreatedAt.toString(), expectedUpdatedAt.toString(), expectedDeleteAt.toString());


         final var actualJson = json.parse(jsonString); 

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeleteAt);
    }
}

