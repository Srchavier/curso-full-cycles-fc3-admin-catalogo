package com.admin.catalogo.application.genre.delete;

import com.admin.catalogo.domain.genre.Genre;

public record DeleteGenreInput(  
    String id
) {
   public static DeleteGenreInput from(final String anId) {
        return new DeleteGenreInput(anId);
   }

   public static DeleteGenreInput from(final Genre aGenre) {
        return new DeleteGenreInput(aGenre.getId().getValue());
   }
}
