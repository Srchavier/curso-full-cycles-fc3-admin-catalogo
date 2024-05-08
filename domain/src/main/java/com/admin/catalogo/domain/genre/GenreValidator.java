package com.admin.catalogo.domain.genre;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;
    

    private final Genre genre;

    protected GenreValidator(final Genre genre, ValidationHandler aHandler) {
        super(aHandler);
        this.genre = genre;
    }

    @Override
    public void validate() {
       checkNameConstraints();

    }

    private void checkNameConstraints() {
        final var name = this.genre.getName();

        if(name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be black"));
            return;
        }

        if((name.length() > NAME_MAX_LENGTH || name.trim().length() < NAME_MIN_LENGTH)) {
            this.validationHandler().append(new Error("'name' must be between 1 and 255 character"));
            return;
        }
    }
}
