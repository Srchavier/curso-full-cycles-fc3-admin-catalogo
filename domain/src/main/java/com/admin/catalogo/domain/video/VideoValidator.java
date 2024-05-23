package com.admin.catalogo.domain.video;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int NAME_MIN_LENGTH_TITLE = 3;
    private static final int NAME_MAX_LENGTH_TITLE = 255;
    private static final int NAME_MIN_LENGTH_DESCRIPTION = 3;
    private static final int NAME_MAX_LENGTH_DESCRIPTION = 4000;

    private Video video;

    protected VideoValidator(final Video aVideo, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = aVideo;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints() {
        final var title = this.video.getTitle();

        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
        }

        if (title != null && title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be blank"));
        }

        if (title != null
                && (title.length() > NAME_MAX_LENGTH_TITLE || title.trim().length() < NAME_MIN_LENGTH_TITLE)) {
            this.validationHandler().append(new Error("'title' must be between %s and %s character"
                    .formatted(NAME_MIN_LENGTH_TITLE, NAME_MAX_LENGTH_TITLE)));
        }
    }

    private void checkDescriptionConstraints() {
        final var description = this.video.getDescription();

        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be empty"));
        }

        if (description != null && description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
        }

        if (description != null && (description.length() > NAME_MAX_LENGTH_DESCRIPTION
                || description.trim().length() < NAME_MIN_LENGTH_DESCRIPTION)) {
            this.validationHandler().append(new Error("'description' must be between %s and %s character"
                    .formatted(NAME_MIN_LENGTH_DESCRIPTION, NAME_MAX_LENGTH_DESCRIPTION)));
        }
    }

    private void checkLaunchedAtConstraints() {
        final var launchedAt = this.video.getLaunchedAt();

        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launcherAt' should not be null"));
        }

    }

    private void checkRatingConstraints() {
        final var rating = this.video.getRating();

        if (rating == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }

    }

}
