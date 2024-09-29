package com.admin.catalogo.application.video.create;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.admin.catalogo.domain.Identifier;
import com.admin.catalogo.domain.castmember.CastMemberGateway;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotificationException;
import com.admin.catalogo.domain.genre.GenreGateway;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.domain.video.Rating;
import com.admin.catalogo.domain.video.Video;
import com.admin.catalogo.domain.video.VideoGateway;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {


	
    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

	private final CastMemberGateway castMemberGateway;

	private final VideoGateway videoGateway;

    public DefaultCreateVideoUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway, final CastMemberGateway castMemberGateway, final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
		this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
		this.videoGateway = Objects.requireNonNull(videoGateway);;
    }

	@Override
	public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
		final var aRating = Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
		final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
		final var genres = toIdentifier(aCommand.genres(), GenreID::from);
		final var members = toIdentifier(aCommand.members(), CastMemberID::from);

		final var notification = Notification.create();
		notification.append(validateCategories(categories));
		notification.append(validateGenres(genres));
		// notification.append(validateMembers(members));

		final var aVideo = Video.newVideo(
			aCommand.title(),
			aCommand.description(), 
			Year.of(aCommand.launchedAt()), 
			aCommand.duration(), 
			aRating, 
			aCommand.opened(), 
			aCommand.published(), 
			categories, 
			genres, 
			members
		 );

		aVideo.validate(notification);

		if(notification.hasError()){
			throw new NotificationException("Could not create Aggregate Video", notification);
		}

        return CreateVideoOutput.with(create(aCommand, aVideo));
	}

	private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
		return this.videoGateway.create(aVideo);
	}

	private Supplier<DomainException> invalidRating(final String rating) {
		return () -> DomainException.with(new Error("rating not found %s".formatted(rating)));
	}

	private <T> Set<T> toIdentifier(Set<String> ids, final Function<String, T> mapper) {
		return ids.stream()
				.map(mapper)
				.collect(Collectors.toSet());
	}

	private ValidationHandler validateCategories(Set<CategoryID> ids) {
		return validateAggregate("categories", ids, categoryGateway::existsByIds);
	}
   
	private ValidationHandler validateGenres(Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
	}

	// private ValidationHandler validateMembers(Set<CastMemberID> ids) {
    //     return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
	// }

	private <T extends Identifier> ValidationHandler validateAggregate(final String aggregate, final Set<T> ids, final Function<Iterable<T>, List<T>> existsByIds) {
		final var notification = Notification.create();

        if(ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if(ids.size() != retrievedIds.size()) {
            final var commandsIds = new ArrayList<>(ids);
            commandsIds.removeAll(retrievedIds);

            final var missingIdsMessages = commandsIds.stream()
                .map(Identifier::getValue)
                .collect(Collectors.joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessages)));

        }

        return notification;
	}

}
