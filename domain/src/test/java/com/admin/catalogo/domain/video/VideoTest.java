package com.admin.catalogo.domain.video;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class VideoTest {


    @Test
    public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        // when

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
            expectedCastMembers
        );


        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getUpdatedAt(), actualVideo.getCreatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    
    @Test
    public void givenValidVideo_whenCallsUpdate_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            "teste",
            "lalalal ssss",
            Year.of(1999),
            0.0,
            Rating.AGE_14,
            true,
            true,
            Set.of(),
            Set.of(),
            Set.of()
        );
        // when

        final var actualVideo = Video.with(aVideo).update(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetVideo_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );
        // when

        final var aVideoMedia = AudioVideoMedia.with("abc", "Video.mp3", "/123/videos", "", MediaStatus.PENDING);

        final var actualVideo = Video.with(aVideo).setVideo(aVideoMedia);

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertEquals(aVideoMedia, actualVideo.getVideo().get());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }


    @Test
    public void givenValidVideo_whenCallsSetTrailer_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );
        // when

        final var aVideoMediaTrailer = AudioVideoMedia.with("trailer", "trailer.mp3", "/123/trailers", "", MediaStatus.COMPLETED);


        final var actualVideo = Video.with(aVideo).setTrailer(aVideoMediaTrailer);

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertEquals(aVideoMediaTrailer, actualVideo.getTrailer().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetBanner_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );
        // when

        final var aBanner = ImageMedia.with("aBanner", "aBanner.mp3", "/123/banners");


        final var actualVideo = Video.with(aVideo).setBanner(aBanner);

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertEquals(aBanner, actualVideo.getBanner().get());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetThumnnail_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );
        // when

        final var aThumbnail = ImageMedia.with("aThumbnail", "aThumbnail.mp3", "/123/thumbnails");


        final var actualVideo = Video.with(aVideo).setThumbnail(aThumbnail);

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertEquals(aThumbnail, actualVideo.getThumbnail().get());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }

    @Test
    public void givenValidVideo_whenCallsSetThumnnailHalf_shouldREturnUpdated() {
        // given
        final var expectedTitle = "Veloses e furiosos";
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

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLauchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            expectedCategories,
            expectedGenres,
            expectedCastMembers
        );
        // when

        final var aThumbnailHalf = ImageMedia.with("aThumbnailHalf", "aThumbnailHalf.mp3", "/123/thumbnailHalfs");


        final var actualVideo = Video.with(aVideo).setThumbnailHalf(aThumbnailHalf);

        // then

        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLauchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.getOpened());
        assertEquals(expectedPublished, actualVideo.getPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedCastMembers, actualVideo.getCastMembers());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertEquals(aThumbnailHalf, actualVideo.getThumbnailHalf().get());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));

    }
}
