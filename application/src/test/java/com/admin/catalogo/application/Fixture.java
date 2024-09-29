package com.admin.catalogo.application;

import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberType;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.video.Rating;
import com.admin.catalogo.domain.video.Resource;

import io.vavr.collection.List;

import static io.vavr.API.*;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2000, 2024);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.0, 3.0, 10.0);
    }

    public static String title() {
        return FAKER.options()
                .option(
                        "Testes unitários do caso de uso de criação de vídeo",
                        "Boas-vindas ao módulo de agregado de video",
                        "TDD da nova entidade de video");
    }

    public static final class Categories {
        public static Category ACAO = Category.newCategory("Acao", "tesdte", true);
        public static Category AULAS = Category.newCategory("Aulas", "some description", true);


        public static Category acao() {
            return ACAO.clone();
        }

        public static Category aulas() {
            return AULAS.clone();
        }

    }

    public static final class Genres {

        public static Genre ACAO = Genre.newGenre("Açào", false);
        public static Genre FICCAO = Genre.newGenre("Ficção", false);
        public static Genre DOC = Genre.newGenre("documentation", false);
        public static Genre SERIE = Genre.newGenre("series", false);

        public static Genre acao() {
            return Genre.with(ACAO);
        }

        public static Genre ficcao() {
            return Genre.with(FICCAO);
        }

        public static Genre doc() {
            return Genre.with(DOC);
        }

        public static Genre serie() {
            return Genre.with(SERIE);
        }
    }

    public static final class CastMembers {

        public static CastMember WESLEY = CastMember.newMember("Wesley full cycle", CastMemberType.ACTOR);

        public static CastMember EDUARDO = CastMember.newMember("eduardo", CastMemberType.DIRECTOR);


        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember eduardo() {
            return CastMember.with(EDUARDO);
        }


        public static CastMemberType type() {
            return FAKER.options()
                    .option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }

    public static final class Videos {

        public static Rating rating() {
            return FAKER.options().option(Rating.AGE_10, Rating.AGE_12, Rating.AGE_14, Rating.AGE_16, Rating.AGE_18,
                    Rating.ER, Rating.L);
        }

        
        public static Resource resource(final Resource.Type type) {

            final String contentType = Match(type).of(
                Case($(List.of(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                Case($(), "image/png")
            );

            final byte[] content = "Conteudo".getBytes();
            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }


        public static String description() {
            return FAKER.options()
                    .option(
                            "The Fast and the Furious (também conhecido como Velozes e Furiosos) é uma franquia de mídia e Universo Compartilhado centrado em uma série de filmes de ação que estão amplamente preocupados com corridas de rua, assaltos, espiões e família.",
                            "O tema principal de Central do Brasil é a separação e a perda. Ambos os personagens principais experimentaram perdas dolorosas, e encontram no \"apego\" que eles desenvolvem gradualmente um com outro, cheios de ambivalência, uma maneira de reparar os danos sofridos anteriormente em suas vidas.");
        }


    }

}
