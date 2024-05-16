package com.admin.catalogo.infrastructure.genre.models;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import com.admin.catalogo.JacksonTest;


@JacksonTest
public class GenreResponseTest {

    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123");
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();

        final var response = new GenreResponse(
            expectedId,
            expectedName,
            expectedCategories,
            expectedActive,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeleteAt);

        final var actualJson = json.write(response);

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.categories_id", expectedCategories)
            .hasJsonPathValue("$.is_active", expectedActive)
            .hasJsonPathValue("$.updated_at", expectedCreatedAt)
            .hasJsonPathValue("$.created_at", expectedUpdatedAt)
            .hasJsonPathValue("$.deleted_at", expectedDeleteAt);

    }
    
    @Test
    public void testUnMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedCategory = "123";
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();
        
        final var jsonString = """
            {
                "id":"%s",
                "name":"%s",
                "categories_id":["%s"],
                "is_active": %s,
                "created_at":"%s",
                "updated_at":"%s",
                "deleted_at":"%s"
            }
            """
            .formatted(expectedId, expectedName, expectedCategory, expectedActive, expectedCreatedAt.toString(), expectedUpdatedAt.toString(), expectedDeleteAt.toString());


         final var actualJson = json.parse(jsonString); 

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
            .hasFieldOrPropertyWithValue("active", expectedActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeleteAt);
    }
}
