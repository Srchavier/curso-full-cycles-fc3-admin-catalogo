package com.admin.catalogo.domain.exceptions;

import java.util.List;

public class DomainException extends NoStacktraceException {

    protected final List<com.admin.catalogo.domain.validation.Error> errors;

    protected DomainException(final String aMessage, List<com.admin.catalogo.domain.validation.Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final com.admin.catalogo.domain.validation.Error anErrors){
        return new DomainException(anErrors.getMessage(), List.of(anErrors));

    }

    public static DomainException with(final List<com.admin.catalogo.domain.validation.Error> anErrors){
        return new DomainException("", anErrors);

    }

    public List<com.admin.catalogo.domain.validation.Error> getErrors() {
        return errors;
    }

}
