package com.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    default boolean hasError() {
        return getErrors() != null && !(getErrors().isEmpty());
    }

    default Error firstError() {
        if(getErrors() != null && !(getErrors().isEmpty())) {
            return getErrors().get(0);
        }
        return null;
    }

    List<Error> getErrors();

    public interface Validation<T> {
        T validate();
    }
    
}
