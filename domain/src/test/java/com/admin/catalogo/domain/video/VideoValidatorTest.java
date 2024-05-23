package com.admin.catalogo.domain.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Year;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = null;
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());

    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "";
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be blank";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

    @Test
    public void givenWithLengthGreaterThan255Title_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "Verose e furiosos";
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 3 and 255 character";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "Verolese e furiosos";
        final String expectedDescription = "";
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "Verolese e furiosos";
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                     The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 3 and 4000 character";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "valoses e furiosos";
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final Year expectedLauchedAt = null;
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launcherAt' should not be null";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = "velosos e furios";
        final var expectedDescription = """
                    The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado
                    centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.
                """;
        final var expectedLauchedAt = Year.of(2010);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLauchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers);

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());
        // when

        final var actualError = assertThrows(DomainException.class, () -> validator.validate());

        // then

        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).getMessage());
    }

}
