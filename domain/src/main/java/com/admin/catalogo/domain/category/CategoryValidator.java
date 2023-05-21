package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;
    
    private Category category;

    protected CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
       checkNameConstraints();

    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if(name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }

        if(name != null && name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be black"));
        }

        if(name != null && (name.length() > NAME_MAX_LENGTH || name.trim().length() < NAME_MIN_LENGTH)) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 character"));
        }
    }
    
}
