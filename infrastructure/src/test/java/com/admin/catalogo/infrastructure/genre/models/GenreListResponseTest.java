package com.admin.catalogo.infrastructure.genre.models;

import java.io.IOException;
import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import com.admin.catalogo.JacksonTest;

@JacksonTest
public class GenreListResponseTest {

    @Autowired
    private JacksonTester<GenreListResponse> json;

    @Test
    public void testMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();

        final var response = new GenreListResponse(
            expectedId,
            expectedName,
            expectedActive,
            expectedCreatedAt,
            expectedDeleteAt);

        final var actualJson = json.write(response);

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.is_active", expectedActive)
            .hasJsonPathValue("$.created_at", expectedCreatedAt)
            .hasJsonPathValue("$.deleted_at", expectedDeleteAt);

    }
    
    @Test
    public void testUnMarshall() throws IOException {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedDeleteAt = Instant.now();
        
        final var jsonString = """
            {
                "id":"%s",
                "name":"%s",
                "is_active": %s,
                "created_at":"%s",
                "deleted_at":"%s"
            }
            """
            .formatted(expectedId, expectedName, expectedActive, expectedCreatedAt.toString(), expectedDeleteAt.toString());


         final var actualJson = json.parse(jsonString); 

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("active", expectedActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeleteAt);
    }
}
