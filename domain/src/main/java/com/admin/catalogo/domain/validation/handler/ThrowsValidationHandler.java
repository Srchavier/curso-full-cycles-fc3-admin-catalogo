package com.admin.catalogo.domain.validation.handler;

import java.util.List;

import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
       try {
        return aValidation.validate();
       } catch (final Exception ex) {
        throw DomainException.with(List.of(new Error(ex.getMessage())));
       }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
    
}
