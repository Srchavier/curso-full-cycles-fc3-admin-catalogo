package com.admin.catalogo.domain.video;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.castmember.CastMemberID;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.genre.GenreID;
import com.admin.catalogo.domain.utils.InstantUtils;
import com.admin.catalogo.domain.validation.ValidationHandler;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final ImageMedia aBanner,
            final ImageMedia aThumbnail,
            final ImageMedia aThumbnailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castmembers) {

        super(anId);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.banner = aBanner;
        this.thumbnail = aThumbnail;
        this.thumbnailHalf = aThumbnailHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castmembers;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new VideoValidator(this, aHandler);
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castmembers) {

        final var now = InstantUtils.now();
        final var anId = VideoID.unique();

        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchedAt,
                aDuration,
                aRating,
                wasOpened,
                wasPublished,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                castmembers
            );
    }

    public static Video with(
            final Video aVideo) {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.getRating(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
            );
    }

  
    public Video update(
        final String aTitle,
        final String aDescription,
        final Year aLaunchedAt,
        final double aDuration,
        final Rating aRating,
        final boolean wasOpened,
        final boolean wasPublished,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> castmembers) {

            this.title = aTitle;
            this.description = aDescription;
            this.launchedAt = aLaunchedAt;
            this.duration = aDuration;
            this.rating = aRating;
            this.opened = wasOpened;
            this.published = wasPublished;
            this.setCategories(categories);;
            this.setGenres(genres);
            this.setCastMembers(castmembers);
            this.updatedAt = InstantUtils.now();

        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Year getLaunchedAt() {
        return this.launchedAt;
    }

    public double getDuration() {
        return this.duration;
    }

    public Rating getRating() {
        return this.rating;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public boolean getOpened() {
        return this.opened;
    }

    public boolean isPublished() {
        return this.published;
    }

    public boolean getPublished() {
        return this.published;
    }
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }
    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(this.banner);
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(this.thumbnail);
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(this.thumbnailHalf);
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(this.trailer);
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(this.video);
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
    }
    public Set<CategoryID> getCategories() {
        return this.categories != null ? Collections.unmodifiableSet(categories): Collections.emptySet();
    }

    public void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories): Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return this.genres != null ? Collections.unmodifiableSet(genres): Collections.emptySet();
    }

    public void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres): Collections.emptySet();
    }

    public Set<CastMemberID> getCastMembers() {
        return this.castMembers != null ? Collections.unmodifiableSet(castMembers): Collections.emptySet();
    }

    public void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers): Collections.emptySet();
    }


}
