package com.admin.catalogo.application;

import com.admin.catalogo.domain.castmember.CastMember;
import com.admin.catalogo.domain.castmember.CastMemberType;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.genre.Genre;
import com.admin.catalogo.domain.video.Rating;
import com.admin.catalogo.domain.video.Resource;
import com.admin.catalogo.domain.video.Video;

import io.vavr.collection.List;

import static io.vavr.API.*;

import java.time.Year;
import java.util.Set;

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

    public static boolean bool() {
        return FAKER.bool().bool();
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

        public static final Genre TECH =
        Genre.newGenre("Technology", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }
        
    }

    public static final class CastMembers {

        public static CastMember WESLEY = CastMember.newMember("Wesley full cycle", CastMemberType.ACTOR);

        public static CastMember EDUARDO = CastMember.newMember("eduardo", CastMemberType.DIRECTOR);

        private static final CastMember GABRIEL =
                CastMember.newMember("Gabriel FullCycle", CastMemberType.ACTOR);

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember gabriel() {
            return CastMember.with(GABRIEL);
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


        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(2022),
                Fixture.duration(),
                rating(),
                Fixture.bool(),
                Fixture.bool(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.gabriel().getId())
        );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.options().option(
                    """
                            Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                            Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                            Para acessar todas as aulas, lives e desafios, acesse:
                            https://imersao.fullcycle.com.br/
                            """,
                    """
                            Nesse vídeo você entenderá o que é DTO (Data Transfer Object), quando e como utilizar no dia a dia, 
                            bem como sua importância para criar aplicações com alta qualidade.
                            """
            );
        }
    }

}
