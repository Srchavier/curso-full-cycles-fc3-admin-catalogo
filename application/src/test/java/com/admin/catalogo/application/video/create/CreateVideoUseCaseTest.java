package com.admin.catalogo.application.video.create;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.admin.catalogo.application.Fixture;
import com.admin.catalogo.application.UseCaseTest;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.video.Resource;
import com.admin.catalogo.domain.video.VideoGateway;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway);
    }

    // @Test
    // public void givenaValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
    //     //given
    //     final var expectedTitle = Fixture.title();
    //     final var expectedDescription = Fixture.Videos.description();
    //     final var expectedLauchedAt = Year.of(2010);
    //     final var expectedDuration = Fixture.duration();
    //     final var expectedOpened = true;
    //     final Boolean expectedPublished = false;
    //     final var expectedRating = Fixture.Videos.rating();
    //     final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.AULAS.getId());
    //     final var expectedGenres = Set.<GenreID>of(Fixture.Genres.ACAO.getId());
    //     final var expectedCastMembers = Set.<CastMemberID>of(Fixture.CastMembers.WESLEY.getId(), Fixture.CastMembers.EDUARDO.getId());

    //     final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
    //     final Resource expectedTrailer =Fixture.Videos.resource(Resource.Type.TRAILER);
    //     final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
    //     final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
    //     final Resource expectThumbhalf = Fixture.Videos.resource(Resource.Type.THUMBNAILHALF);


    //     final var aCommand = CreateVideoCommand.with(
    //         expectedTitle,
    //         expectedDescription,
    //         expectedLauchedAt.getValue(),
    //         expectedDuration,
    //         expectedOpened,
    //         expectedPublished,
    //         expectedRating.getName(),
    //         asString(expectedCategories),
    //         asString(expectedGenres),
    //         asString(expectedCastMembers),
    //         expectedVideo,
    //         expectedTrailer,
    //         expectedBanner,
    //         expectedThumb,
    //         expectThumbhalf
    //     );


    //     //when

    //     when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    //     when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    //     when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCastMembers));

    //     when(videoGateway.create(any())).thenAnswer(returnsFirstArg());



    //     final var actualResult = useCase.execute(aCommand);

    //     // then

    //     assertNotNull(actualResult);
    //     assertNotNull(actualResult.id());


    //     verify(videoGateway).create(argThat(actualVideo -> 
    //         Objects.equals( expectedTitle, actualVideo.getTitle())
    //          && Objects.equals(expectedDescription,actualVideo.getDescription())
    //          && Objects.equals(expectedLauchedAt,actualVideo.getLaunchedAt())
    //          && Objects.equals(expectedDuration,actualVideo.getDuration())
    //          && Objects.equals(expectedOpened,actualVideo.getOpened())
    //          && Objects.equals(expectedPublished,actualVideo.getPublished())
    //          && Objects.equals(expectedRating, actualVideo.getRating())
    //          && Objects.equals(expectedCategories, actualVideo.getCategories())
    //          && Objects.equals(expectedGenres, actualVideo.getGenres())
    //          && Objects.equals(expectedCastMembers, actualVideo.getCastMembers())
    //         //  && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
    //         //  && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
    //         //  && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
    //         //  && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
    //         //  && Objects.equals(expectThumbhalf.name(), actualVideo.getThumbnailHalf().get().name())
    //         //  && actualVideo.getVideo().isPresent()
    //         //  && actualVideo.getTrailer().isPresent()
    //         //  && actualVideo.getBanner().isPresent()
    //         //  && actualVideo.getThumbnail().isPresent()
    //         //  && actualVideo.getThumbnailHalf().isPresent()
             
    //     ));

    // }


}
